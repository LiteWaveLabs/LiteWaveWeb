package de.niklasfauth.litewave.analyze;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.niklasfauth.litewave.DatabaseConnection;
import de.niklasfauth.litewave.objects.ConcSpectraPlotValues;
import de.niklasfauth.litewave.objects.SpectraPlotValues;

public class ConcAnalyze implements Runnable {
	@Override
	public void run() {

		String elements = ConcSpectraPlotValues.getConfig();

		LinkedList<Double> valueX = SpectraPlotValues.getRawPlot()[0];
		LinkedList<Double> valueY = SpectraPlotValues.getRawPlot()[1];
		Map<Integer, String> labels = new HashMap<Integer, String>();

		LinkedList<double[]> searchWavelenghts = new LinkedList<double[]>();
		LinkedList<String> searchNames = new LinkedList<String>();

		String[] parts = elements.split(";");

		int numElements = ConcSpectraPlotValues.getNumConfig();
		int refPeak;
		ConcSpectraPlotValues.getPieList().clear();
		try {
			for (int i = 0; i < parts.length; i++) {

				if (parts[i].equals("H")) { // turned out H% also matches with Hg (Mercury). Pls kill me
					parts[i] = "H ";
				}
				
				if (parts[i].equals("N")) { // turned out N% also matches with Na (Natrium).
					parts[i] = "N ";
				}
				
				if (parts[i].equals("B")) { // turned out B% also matches with Ba (Barium).
					parts[i] = "B ";
				}
				
				if (parts[i].equals("C")) { // turned out C% also matches with Ca (Calcium).
					parts[i] = "C ";
				}
				
				if (parts[i].equals("F")) { // turned out F% also matches with Fe (Iron).
					parts[i] = "F ";
				}
				
				if (parts[i].equals("S")) { // turned out S% also matches with Sn (Tin).
					parts[i] = "S ";
				}
				
				if (parts[i].equals("O")) { // turned out O% also matches with Os (Osmium).
					parts[i] = "O ";
				}
				
				if (parts[i].equals("P")) { // turned out P% also matches with Pbs (Lead).
					parts[i] = "P ";
				}

				PreparedStatement prep = DatabaseConnection.getInstance()
						.prepare(
								"SELECT * FROM asd WHERE element LIKE '"
										+ parts[i]
										+ "%' ORDER BY intensity DESC LIMIT "
										+ numElements + ";");
				ResultSet rs = prep.executeQuery();

				searchNames.add(parts[i]);
				System.out.println(searchNames.get(i));
				double[] tempWaves = new double[10];
				int a = 0;
				int concPerElements = 0;
				int peaksPerElement = 0;
				while (rs.next()) {
					peaksPerElement++;
					// Retrieve by column name
					tempWaves[a] = rs.getFloat("wavelength");
					System.out.println(tempWaves[a]);
					int cnt = 0;
					while (tempWaves[a] >= valueX.get(cnt)) {
						cnt++;
					}

					// Spectrometer is nonlinear. Horrible hack, pls fix soon!
					if (cnt < 700) {
						labels.put(cnt + 1, parts[i]);
						concPerElements += (valueY.get(cnt + 1) - ConcSpectraPlotValues
								.getOffConfig());
					}

					else {
						labels.put(cnt - 1, parts[i]);
						concPerElements += (valueY.get(cnt - 1) - ConcSpectraPlotValues
								.getOffConfig());
					}

					System.out.println(parts[i]);
					System.out.println(concPerElements);

					a++;
				}
				concPerElements = (int) ((concPerElements / peaksPerElement) * ConcSpectraPlotValues
						.getScaleConfig());

				ConcSpectraPlotValues.getPieList().put(parts[i],
						(double) concPerElements);
				searchWavelenghts.add(i, tempWaves);
				rs.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int cnt = 0;
		while (ConcSpectraPlotValues.getRefConfig() >= valueX.get(cnt)) {
			cnt++;
		}
		refPeak = valueY.get(cnt - 1).intValue();
		System.out.println("Referenz = " + refPeak);

		ConcSpectraPlotValues.setRefPeak(refPeak);

		String peakPlot = "[";
		String peakList = "formatter: function() {var tooltip=null;";

		int peakCounter = 0;

		for (int value = 0; value < 1024; value++) {
			String labelName = labels.get(value);

			if (labelName != null) {

				peakCounter++;
				peakPlot += "{y:" + valueY.get(value) + ", name: 'peak"
						+ peakCounter + "'},"; // peakResult +=

				double wavelenght = valueX.get(value);
				System.out.println(wavelenght);

				peakList += "if (this.key=='peak" + peakCounter
						+ "'){tooltip = 'Peak " + peakCounter + ": "
						+ String.format("%.2f", wavelenght) + "nm<br>"
						+ labelName + "'}\n";

			} else {
				peakPlot += null + ",";
			}
		}

		peakPlot += "]";
		peakList += "return tooltip;}";
		ConcSpectraPlotValues.setPeakList(peakList);
		ConcSpectraPlotValues.setPeakPlot(peakPlot);

	}
}

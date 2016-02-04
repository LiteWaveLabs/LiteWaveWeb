package de.niklasfauth.litewave.analyze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.niklasfauth.litewave.objects.SpectraPlotValues;

public class Analyze implements Runnable {
	@Override
	public void run() {
		double threshold = SpectraPlotValues.getConfig()[0];
		double maxOffset = SpectraPlotValues.getConfig()[1];
		int elementsPerPeak = (int) SpectraPlotValues.getConfig()[2];

		LinkedList<Double> valueX = SpectraPlotValues.getRawPlot()[0];
		LinkedList<Double> valueY = SpectraPlotValues.getRawPlot()[1];

		Map<Integer, Double> maxima = new HashMap<Integer, Double>();

		maxima = LiteWaveUtils.peakDetection(valueY, threshold).get(0); // Maximas

		String result = "";
		String peakResult = "[";
		String peakList = "formatter: function() {var tooltip=null;";

		int peakCounter = 0;

		for (int value = 0; value < valueY.size(); value++) {
			Double peak = maxima.get(value);
			if (peak != null) {

				peakCounter++;
				peakResult += "{y:" + valueY.get(value) + ", name: 'peak"
						+ peakCounter + "'},";
				// peakResult += valueY.get(value) + ",";

				double wavelenght = valueX.get(value);
				System.out.println(wavelenght);
				result += "<p>" + wavelenght + "nm</p>";
				ArrayList<PeakLabel> label = ElementFinder.mySQLHandler(
						wavelenght, elementsPerPeak, maxOffset);

				for (int i = 0; i < label.size(); i++) {
					result += "<p>" + (i + 1) + ". Element: "
							+ label.get(i).getElement() + "    Intensität: "
							+ label.get(i).getIntensity() + "    Wellenlänge: "
							+ label.get(i).getWavelength() + "nm</p>";
					if (i == 0) {
						peakList += "if (this.key=='peak"
								+ peakCounter
								+ "'){tooltip = 'Peak "
								+ peakCounter
								+ ": "
								+ String.format("%.2f", label.get(i)
										.getWavelength()) + "nm<br>";

					}
					peakList += "<b>" + (i + 1) + ": "
							+ label.get(i).getElement() + "</b><br>";

					if (i == label.size() - 1) {
						peakList += "'}\n";
					}
				}
				result += "<br>";
			} else {
				peakResult += null + ",";
			}
		}
		peakResult += "]";
		peakList += "return tooltip;}";
		SpectraPlotValues.setPeakList(peakList);
		SpectraPlotValues.setPeakPlot(peakResult);
		SpectraPlotValues.setResultString(result);
	}

}

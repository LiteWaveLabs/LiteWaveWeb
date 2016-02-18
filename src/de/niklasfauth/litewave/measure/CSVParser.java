package de.niklasfauth.litewave.measure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import de.niklasfauth.litewave.objects.SpectraPlotValues;

public class CSVParser {

	public static void parse() throws IOException {

		LinkedList<Double> wavelengthsList = new LinkedList<Double>();
		LinkedList<Double> spectralDataList = new LinkedList<Double>();

		BufferedReader br;
		br = new BufferedReader(new FileReader("data/spectrum.csv"));

		String line = null;
		try {
			line = br.readLine();
			while (line != null) {
				wavelengthsList.add(Double.parseDouble(line.split(",")[0]));
				spectralDataList.add(Double.parseDouble(line.split(",")[1]));

				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			br.close();
		}
		SpectraPlotValues.setRawPlot(wavelengthsList, spectralDataList);

	}
}

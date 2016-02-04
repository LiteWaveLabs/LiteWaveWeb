package de.niklasfauth.litewave.analyze;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {
	public static void main(String[] args) throws IOException {

		double threshold = 400.0;
		double maxOffset = 0.3;
		int elementsPerPeak = 5;
		

		LinkedList<Double> valueX = new LinkedList<Double>();
		LinkedList<Double> valueY = new LinkedList<Double>();

		valueX = parseFile("outputX.txt");
		valueY = parseFile("outputY.txt");

		Map<Integer, Double> maxima = new HashMap<Integer, Double>();

		maxima = LiteWaveUtils.peakDetection(valueY, threshold).get(0); // Maximas

		for (int value = 0; value < valueY.size(); value++) {
			Double peak = maxima.get(value);
			if (peak != null) {
				double wavelenght = valueX.get(value);
				System.out.println(wavelenght);
				ArrayList<PeakLabel> label = ElementFinder.mySQLHandler(
						wavelenght, elementsPerPeak, maxOffset);
				
				for (int i = 0; i < label.size(); i++) {
					System.out.println(i + ". Element: " + label.get(i).getElement() + "    Intensität: " + label.get(i).getIntensity()+ "    Wellenlänge: " + label.get(i).getWavelength());
				}
			}
		}
	}

	public static LinkedList<Double> parseFile(String fileName) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		LinkedList <Double> outputList = new LinkedList <Double>();
		try {
			line = br.readLine();
			while (line != null) {
				double d = Double.parseDouble(line);
				// System.out.println(f);
				outputList.add(d);
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return outputList;
	}
}

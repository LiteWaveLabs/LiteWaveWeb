package de.niklasfauth.litewave.objects;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ConcSpectraPlotValues implements Serializable {
	private static LinkedList<Double> wavelength;
	private static LinkedList<Double> rawValues;

	private static LinkedList<Double> wavelengthPeak;
	private static LinkedList<Double> valuesPeak;

	private static LinkedHashMap<String, Double> concPieList = new LinkedHashMap<String,Double>();
	
	private static String elements;
	private static int numElements;
	private static int offset;
	private static int refPeak;

	private static String resultString = "";

	private static String peakResultString = "[]";

	private static String peakResultList = "";

	private static double reference;
	
	private static double scale;

	private static LinkedList<Double> peakWavelength = new LinkedList<Double>();

	/*
	 * private int intensity; private String element; private double wavelength;
	 */

	public ConcSpectraPlotValues(LinkedList<Double> wavelength,
			LinkedList<Double> rawValues) {
		this.wavelength = wavelength;
		this.rawValues = rawValues;
	}

	public static LinkedList getWavelength() {
		return wavelength;
	}

	public static LinkedList getRawValues() {
		return rawValues;
	}

	public static LinkedList[] getRawPlot() {
		LinkedList listArray[] = new LinkedList[2];
		listArray[0] = wavelength;
		listArray[1] = rawValues;
		return listArray;
	}

	public static String getConfig() {

		return elements;
	}

	public static int getNumConfig() {

		return numElements;
	}
	
	public static int getOffConfig() {

		return offset;
	}

	public static double getRefConfig() {

		return reference;
	}
	
	public static double getScaleConfig() {

		return scale;
	}

	public static void setRawPlot(LinkedList<Double> parseFile,
			LinkedList<Double> parseFile2) {
		wavelength = parseFile;
		rawValues = parseFile2;
	}

	public static String getPeakPlot() {
		return peakResultString;
	}

	public static void setPeakPlot(String parseFile2) {

		peakResultString = parseFile2;
	}

	public static void setConfig(String input, int count, double bla, int blub, double d) {
		elements = input;
		numElements = count;
		reference = bla;
		offset = blub;
		scale = d;
	}
	
	public static void setPieList(LinkedHashMap<String, Double> peakList) {
		// TODO Auto-generated method stub
		concPieList = peakList;
	}

	public static LinkedHashMap<String, Double> getPieList() {
		// TODO Auto-generated method stub
		return concPieList;
	}

	/*
	 * public static void setResultString(String string) { resultString =
	 * string; }
	 * 
	 * public static String getResultString() { return resultString; }
	 */
	public static void setPeakList(String peakList) {
		// TODO Auto-generated method stub
		peakResultList = peakList;
	}

	public static String getPeakList() {
		// TODO Auto-generated method stub
		return peakResultList;
	}

	public static int getRefPeak() {
		// TODO Auto-generated method stub
		return refPeak;
	}

	public static void setRefPeak(int refPeak2) {
		// TODO Auto-generated method stub
		refPeak = refPeak2;
	}

}
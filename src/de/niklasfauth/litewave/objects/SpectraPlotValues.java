package de.niklasfauth.litewave.objects;

import java.io.Serializable;
import java.util.LinkedList;

public class SpectraPlotValues implements Serializable {
	private static LinkedList<Double> wavelength;
	private static LinkedList<Double> rawValues;

	private static LinkedList<Double> wavelengthPeak;
	private static LinkedList<Double> valuesPeak;

	private static float offset;
	private static float maxOffset;
	private static float perPeak;
	private static float orderBy;
	private static float coefficient;
	
	private static String resultString = "";

	private static String peakResultString = "[]";

	private static String peakResultList = "";

	private static LinkedList<Double> peakWavelength = new LinkedList<Double>();

	/*
	 * private int intensity; private String element; private double wavelength;
	 */

	public SpectraPlotValues(LinkedList<Double> wavelength,
			LinkedList<Double> rawValues) {
		this.wavelength = wavelength;
		this.rawValues = rawValues;
	}

	public static LinkedList<Double> getWavelength() {
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

	public static float[] getConfig() {
		float config[] = { 0, 0, 0, 0, 0 };
		config[0] = offset;
		config[1] = maxOffset;
		config[2] = perPeak;
		config[3] = orderBy;
		config[4] = coefficient;
		return config;
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

	public static void setConfig(float offsetIn, float maxOffsetIn,
			float perPeakIn, float i, float f) {
		offset = offsetIn;
		perPeak = perPeakIn;
		maxOffset = maxOffsetIn;
		orderBy = i;
		coefficient = f;
	}

	public static void setResultString(String string) {
		resultString = string;
	}

	public static String getResultString() {
		return resultString;
	}

	public static void setPeakList(String peakList) {
		// TODO Auto-generated method stub
		peakResultList = peakList;
	}

	public static String getPeakList() {
		// TODO Auto-generated method stub
		return peakResultList;
	}

}
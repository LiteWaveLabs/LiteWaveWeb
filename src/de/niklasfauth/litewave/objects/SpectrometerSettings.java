package de.niklasfauth.litewave.objects;

import java.io.Serializable;

import com.oceanoptics.omnidriver.api.wrapper.Wrapper;

public class SpectrometerSettings implements Serializable {
	
	private static Wrapper wrapper;
	private static int[] settings = {1, 0};
	/*
	private static LinkedList<Double> wavelength;
	private static LinkedList<Double> rawValues;

	private static LinkedList<Double> wavelengthPeak;
	private static LinkedList<Double> valuesPeak;

	private static float offset;
	private static float maxOffset;
	private static float perPeak;

	private static String resultString = "";

	private static String peakResultString = "[]";

	private static String peakResultList = "";

	private static LinkedList<Double> peakWavelength = new LinkedList<Double>();
*/
	
	public SpectrometerSettings(Wrapper configWrapper) {
		this.wrapper = configWrapper;
	}

	public static Wrapper getSpectrometerWrapper() {
		return wrapper;
	}
	
	public static void setSpectrometerSettings(int avgct, int integrationTime) {
		settings[0] = avgct;
		settings[1] = integrationTime;
	}
	
	public static int[] getSpectrometerSettings() {
		return settings;
	}


}
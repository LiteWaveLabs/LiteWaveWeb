package de.niklasfauth.litewave.measure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import com.oceanoptics.omnidriver.api.wrapper.Wrapper;
import com.oceanoptics.omnidriver.features.boardtemperature.BoardTemperature;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.RaspiPin;

import de.niklasfauth.litewave.objects.SpectraPlotValues;
import de.niklasfauth.litewave.objects.SpectrometerSettings;
import de.niklasfauth.litewave.utils.JSONupdater;

public class Measure implements Runnable {
	@Override
	public void run() {

		// For ARM based systems SeaBreeze must be used

		int avgCt = SpectrometerSettings.getSpectrometerSettings()[0];
		int integrationTime = SpectrometerSettings.getSpectrometerSettings()[1];

		// For intel based architectures OmniDriver should be used:

		// set the integration time

		System.out.println("Anzahl Messungen: " + avgCt);
		System.out.println("Integrationszeit: " + integrationTime);

		LinkedList<Double> wavelengthsList = new LinkedList<Double>();
		LinkedList<Double> spectralDataList = new LinkedList<Double>();
		double[] wavelengths, spectralData; // arrays of doubles to hold the

		Wrapper wrapper = SpectrometerSettings.getSpectrometerWrapper();
		int numberOfPixels = 0;
		BoardTemperature boardTemperature;
		double temperatureCelsius;
		boardTemperature = wrapper.getFeatureControllerBoardTemperature(0);
		try {
			temperatureCelsius = boardTemperature.getBoardTemperatureCelsius();
			System.out.println("board temperature = " + temperatureCelsius);
		} catch (IOException ioException) {
			System.out
					.println("The following exception occurred while attempting to obtain the board temperature");
			System.out.println(ioException);
		}
		
		numberOfPixels = wrapper.getNumberOfPixels(0);

		System.out.println("number of pixels = " + numberOfPixels);
		// get a spectrum from the spectrometers:
		wrapper.setIntegrationTime(0, integrationTime);
		wrapper.setScansToAverage(0, avgCt);
		wrapper.setCorrectForElectricalDark(0, 0);
		wrapper.setStrobeEnable(0,1);
		wrapper.setExternalTriggerMode(0,1);
		
		System.out.println("starte spektrometer");
		wavelengths = wrapper.getWavelengths(0);
		
		System.out.println("mitte spektrometer");
		spectralData = wrapper.getSpectrum(0);

		System.out.println("stoppe spektrometer");
		 // gets the number of
		// pixels in the first // spectrometer.

		for (int i = 0; i < numberOfPixels; i++) {
			wavelengthsList.add(wavelengths[i]);
			spectralDataList.add(spectralData[i]);
		}

		SpectraPlotValues.setRawPlot(wavelengthsList, spectralDataList);
		//JSONupdater.setJSONState(true);
	}
}

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

public class Measure implements Runnable {
	@Override
	public void run() {

		// For ARM based systems SeaBreeze must be used

		int avgCt = SpectrometerSettings.getSpectrometerSettings()[0];
		int integrationTime = SpectrometerSettings.getSpectrometerSettings()[1];

		final GpioController gpio = GpioFactory.getInstance();

		final GpioPinDigitalOutput pinHV = gpio.provisionDigitalOutputPin(
				RaspiPin.GPIO_06, "High voltage", PinState.LOW);
		final GpioPinDigitalOutput pinUS = gpio.provisionDigitalOutputPin(
				RaspiPin.GPIO_11, "Ultrasonic", PinState.LOW);

		pinHV.setShutdownOptions(true, PinState.LOW);
		pinUS.setShutdownOptions(true, PinState.LOW);

		pinUS.high();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		pinHV.high(); 

		try {
			Thread.sleep(2500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String execute = "cd data; ../data-collection --step 1:"
				+ integrationTime + ":" + avgCt;

		Process p;
		try {
			p = Runtime.getRuntime().exec(
					new String[] { "bash", "-c", execute });
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}

		try {
			CSVParser.parse();
		} catch (IOException e) { // TODO

			e.printStackTrace();
		}
		pinUS.low();
		pinHV.low();
		gpio.shutdown();
		gpio.unprovisionPin(pinHV);
		gpio.unprovisionPin(pinUS);

		// For intel based architectures OmniDriver should be used:

		/*
		 * int avgCt = SpectrometerSettings.getSpectrometerSettings()[0]; int
		 * integrationTime = SpectrometerSettings.getSpectrometerSettings()[1];
		 * // set the integration time
		 * 
		 * System.out.println("Anzahl Messungen: " + avgCt);
		 * System.out.println("Integrationszeit: " + integrationTime);
		 * 
		 * LinkedList<Double> wavelengthsList = new LinkedList<Double>();
		 * LinkedList<Double> spectralDataList = new LinkedList<Double>();
		 * double[] wavelengths, spectralData; // arrays of doubles to hold the
		 * 
		 * Wrapper wrapper = SpectrometerSettings.getSpectrometerWrapper(); int
		 * numberOfPixels; BoardTemperature boardTemperature; double
		 * temperatureCelsius; boardTemperature =
		 * wrapper.getFeatureControllerBoardTemperature(0); try {
		 * temperatureCelsius = boardTemperature.getBoardTemperatureCelsius();
		 * System.out.println("board temperature = " + temperatureCelsius); }
		 * catch (IOException ioException) { System.out .println(
		 * "The following exception occurred while attempting to obtain the board temperature"
		 * ); System.out.println(ioException); }
		 * 
		 * // get a spectrum from the spectrometers:
		 * wrapper.setIntegrationTime(0, integrationTime);
		 * wrapper.setScansToAverage(0, avgCt);
		 * wrapper.setCorrectForElectricalDark(0, 0);
		 * 
		 * wavelengths = wrapper.getWavelengths(0); spectralData =
		 * wrapper.getSpectrum(0);
		 * 
		 * numberOfPixels = wrapper.getNumberOfPixels(0); // gets the number of
		 * // pixels in the first // spectrometer.
		 * 
		 * // loop for printing the spectral data to the screen: for (int i = 0;
		 * i < numberOfPixels; i++) { wavelengthsList.add(wavelengths[i]);
		 * spectralDataList.add(spectralData[i]); }
		 * 
		 * SpectraPlotValues.setRawPlot(wavelengthsList, spectralDataList);
		 */
	}
}

package de.niklasfauth.litewave.analyze;
import java.io.Serializable;

public class PeakLabel implements Serializable {
	private int intensity;
	private String element;
	private double wavelength;
	private long probability;

	public PeakLabel(int intensity, String element, double wavelength, long probability) {
		this.intensity = intensity;
		this.element = element;
		this.wavelength = wavelength;
		this.probability = probability;
	}

	public int getIntensity() {
		return intensity;
	}

	public String getElement() {
		return element;
	}
	
	public double getWavelength() {
		return wavelength;
	}
	
	public long getProbability() {
		return probability;
	}
}
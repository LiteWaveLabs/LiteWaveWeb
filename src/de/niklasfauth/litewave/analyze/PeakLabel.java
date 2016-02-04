package de.niklasfauth.litewave.analyze;
import java.io.Serializable;

public class PeakLabel implements Serializable {
	private int intensity;
	private String element;
	private double wavelength;

	public PeakLabel(int intensity, String element, double wavelength) {
		this.intensity = intensity;
		this.element = element;
		this.wavelength = wavelength;
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
}
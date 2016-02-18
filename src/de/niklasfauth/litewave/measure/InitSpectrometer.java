package de.niklasfauth.litewave.measure;

import com.oceanoptics.omnidriver.api.wrapper.Wrapper;

import de.niklasfauth.litewave.objects.SpectrometerSettings;

public class InitSpectrometer implements Runnable {
	@Override
	public void run() { 
		
		Wrapper wrapper = new Wrapper(); // Create a wrapper object
		wrapper.openAllSpectrometers(); 
		String serialNumber = wrapper.getSerialNumber(0);
		System.out.println("Serial Number: " + serialNumber); 
		new SpectrometerSettings(wrapper);
	}
}

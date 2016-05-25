package de.niklasfauth.litewave.measure;

import com.oceanoptics.omnidriver.api.wrapper.Wrapper;

import de.niklasfauth.litewave.objects.SpectrometerSettings;
import de.niklasfauth.litewave.utils.QueueSharer;

public class InitSpectrometer implements Runnable {
	@Override
	public void run() { 
		
		Wrapper wrapper = new Wrapper(); // Create a wrapper object
		wrapper.openAllSpectrometers(); 
		String serialNumber = wrapper.getSerialNumber(0);
		System.out.println("Serial Number: " + serialNumber); 
		new SpectrometerSettings(wrapper);
		try {
			QueueSharer.getQueue().put("g+0");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package de.niklasfauth.litewave.utils;

import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

public class JSONupdater implements Runnable {

	
    private LinkedBlockingQueue queue;
    private boolean running;

    
    public JSONupdater(LinkedBlockingQueue queue) {
        this.queue = queue;
    }
    
	public void run() {
		while (true) {
			try {
				queue.put("b+0");
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	private static double batVoltage;
	private static int capVoltage;
	private static int errorType = 255;
    private static boolean measureState;
	public static JSONObject getJSON() {

		JSONObject obj = new JSONObject();
		obj.put("bat", batVoltage);
		obj.put("state", measureState);
		obj.put("error", errorType);
		obj.put("cap", capVoltage);
		return obj;
	}
	
	public static void setJSONBattery(double voltage) {
		batVoltage = voltage;
	}
	
	public static void setJSONState(boolean state) {
		measureState = state;
	}
	
	public static void setJSONCap(int voltage) {
		capVoltage = voltage;
	}
	
	public static void setJSONError(int error) {
		errorType = error;
	}

}
package de.niklasfauth.litewave.utils;

import gnu.io.*;

//import javax.comm.*; 
import java.util.Enumeration;
import java.util.Random;
import java.io.*;
import java.util.TooManyListenersException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.LinkedBlockingQueue;

public class HardwareInterface implements Runnable, SerialPortEventListener {

	private LinkedBlockingQueue queue;

	private boolean responseState;

	public HardwareInterface(LinkedBlockingQueue queue) {
		this.queue = queue;
	}

	private String task;
	@Override
	public void run() {

		// As long as the producer is running,
		// we want to check for elements.

		initialize();
		long startTime = 0;
		while (true) {

			if (responseState
					|| (System.currentTimeMillis() - startTime) > 5000) {
				try {
					//System.out.println("OC\tElements right now: " + queue);
					startTime = System.currentTimeMillis(); // fetch starting
															// time
					task = (String) queue.take();
					//System.out.println("OC\tWorking on: " + task);
					responseState = false;
					send(task);

				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}
	}

	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/ttyUSB0" // Windows
	};
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 57600;

	private boolean serialOpen;

	public void initialize() {
		// the next line is for Raspberry Pi and
		// gets us into the while loop and was suggested here was suggested
		// http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
		 //System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyUSB0");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
			System.out.println("Available:");
			System.out.println(currPortId.getName());
			for (String portName : PORT_NAMES) {
				System.out.println("Com ports: " + currPortId.getName());
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(
					serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			serialOpen = true;
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public synchronized void send(String nachricht) {
		if (!serialOpen)
			return;
		System.out.println("Sende: " + nachricht);
		nachricht = nachricht + "\n";
		try {
			output.write(nachricht.getBytes());
		} catch (IOException e) {
			System.err.println("Fehler beim Senden");
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = input.readLine();
				
				responseState = true;
				System.out.println(inputLine);

				if (inputLine.charAt(0) == '!') {
					System.out.println("Done charging");
					JSONupdater.setJSONState(true);
				}
				
				if (inputLine.charAt(0) == 'c') {
					JSONupdater.setJSONCap(Integer.parseInt(inputLine.substring(2)));
				}
				
				if (inputLine.charAt(0) == 'e') {
					JSONupdater.setJSONError(Integer.parseInt(inputLine.substring(2)));
				}
				
				if (inputLine.charAt(0) == 'b') {
					JSONupdater.setJSONBattery(Double.parseDouble(inputLine.substring(2)));
				}
				System.out.println(JSONupdater.getJSON());

			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}
}

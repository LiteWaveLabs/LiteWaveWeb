package de.niklasfauth.litewave.pages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.dbaufzug.Page;

import org.json.JSONException;
import org.json.JSONObject;

import de.niklasfauth.litewave.measure.Measure;
import de.niklasfauth.litewave.objects.SpectraPlotValues;

public class ChargePage extends Page {

	public ChargePage() {
		super("Charging...");
		
	}
	
	static int timeout = 0;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException, JSONException,
			SQLException {
		
		
		getDefaultTemplate().output(resp.getWriter(), vars);
		
		System.out.println("Timeout: " + timeout);


	}

	@Override
	public boolean needsTemplate() {
		return true;
	}
	
	public static void setTimeout(int Timeout) {
		timeout = Timeout;
	}


	

}

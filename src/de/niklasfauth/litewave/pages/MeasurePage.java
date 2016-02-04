package de.niklasfauth.litewave.pages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.dbaufzug.Page;
import me.streib.janis.dbaufzug.pages.statistics.APIAvailibility;

import org.json.JSONException;

import de.niklasfauth.litewave.analyze.Analyze;
import de.niklasfauth.litewave.measure.Measure;
import de.niklasfauth.litewave.objects.SpectraPlotValues;
import de.niklasfauth.litewave.pages.visualizing.Spectra;

public class MeasurePage extends Page {
	//private Availibility availStat = new Availibility();
//	private Spectra rawSpectra = new Spectra();
	private APIAvailibility apiStat = new APIAvailibility();
	

	public MeasurePage() throws IOException {
		super("Statistiken");
		SpectraPlotValues.setRawPlot(parseFile("outputX.txt"), parseFile("outputY.txt"));
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {

		getDefaultTemplate().output(resp.getWriter(), vars);
		
		
		
		
		//req.getSession().setAttribute("plotdata", arg1);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException{
		System.out.println(req.getParameter("expose"));
		System.out.println(req.getParameter("avgct"));

		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			executor.submit(new Measure()).get(10, TimeUnit.SECONDS);
			resp.sendRedirect("/stats");
		} catch (InterruptedException | ExecutionException | TimeoutException e1) {
			resp.getWriter().print("Fehler: Timeout");
			e1.printStackTrace();
		} // Timeout of 10 minutes.
		executor.shutdown();
	}
	

	@Override
	public boolean needsTemplate() {
		return true;
	}
	
	
	public static LinkedList<Double> parseFile(String fileName) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		LinkedList <Double> outputList = new LinkedList <Double>();
		try {
			line = br.readLine();
			while (line != null) {
				double d = Double.parseDouble(line);
				// System.out.println(f);
				outputList.add(d);
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return outputList;
	}
}

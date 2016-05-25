package de.niklasfauth.litewave.pages;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.dbaufzug.Page;
import me.streib.janis.dbaufzug.pages.statistics.APIAvailibility;
import de.niklasfauth.litewave.measure.CSVParser;
import de.niklasfauth.litewave.measure.Measure;
import de.niklasfauth.litewave.objects.SpectraPlotValues;
import de.niklasfauth.litewave.objects.SpectrometerSettings;
import de.niklasfauth.litewave.utils.JSONupdater;
import de.niklasfauth.litewave.utils.QueueSharer;

public class MeasurePage extends Page {
	// private Availibility availStat = new Availibility();
	// private ElementSpectra rawSpectra = new ElementSpectra();
	private APIAvailibility apiStat = new APIAvailibility();

	public MeasurePage() throws IOException {
		super("Messung");
		try {
			CSVParser.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {

		getDefaultTemplate().output(resp.getWriter(), vars);

		// req.getSession().setAttribute("plotdata", arg1);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {

		int measureTime = Math.round(Float.parseFloat(req
				.getParameter("expose")) * 1000000);
		int avgct = Integer.parseInt(req.getParameter("avgct"));

		int volts = Integer.parseInt(req.getParameter("volt"));

		if (avgct == 1337) {
			resp.sendRedirect("/1337");
			return;
		}

		if (volts < 450) {
			SpectrometerSettings.setSpectrometerSettings(avgct, measureTime);

			int timeout = (((measureTime / 1000000) * avgct) * 3) + 10;

			System.out.println("Timeout: " + timeout);
			ChargePage.setTimeout(timeout);

			JSONupdater.setJSONState(false);

			Thread measure = new Thread(new Measure());
			measure.start();

			SpectraPlotValues.setPeakList("");
			SpectraPlotValues.setPeakPlot("[]");
			SpectraPlotValues.setResultString("");

			JSONupdater.setJSONError(255);
			try {
				QueueSharer.getQueue().put("c+" + volts);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			resp.sendRedirect("/charge");
		}

	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

}

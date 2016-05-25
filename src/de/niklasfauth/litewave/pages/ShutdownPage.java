package de.niklasfauth.litewave.pages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import de.niklasfauth.litewave.objects.SpectrometerSettings;

public class ShutdownPage extends Page {

	public ShutdownPage() {
		super("Shutdown options");
		
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException, JSONException,
			SQLException {
		getDefaultTemplate().output(resp.getWriter(), vars);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException {
		
		if (req.getParameter("shutdown") != null) {
			Process p;
			try {
				p = Runtime.getRuntime().exec(
						new String[] { "systemctl", "poweroff" });
				//p.waitFor();

			} catch (Exception e) {
				e.printStackTrace();
			}
			resp.sendRedirect("/bye");
		}
		
        if (req.getParameter("restart") != null) {

			Process p;
			try {
				p = Runtime.getRuntime().exec(
						new String[] { "systemctl", "reboot"});
				//p.waitFor();

			} catch (Exception e) {
				e.printStackTrace();
			}
			resp.sendRedirect("/bye");
		}
        
        if (req.getParameter("java") != null) {

			Process p;
			try {
				p = Runtime.getRuntime().exec(
						new String[] { "/bin/bash", "-c", "sudo /home/lutewave/LiteWave/reset.sh"});
				//p.waitFor();

			} catch (Exception e) {
				e.printStackTrace();
			}
			resp.sendRedirect("/bye");
		}
        getDefaultTemplate().output(resp.getWriter(), vars);
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}
	

}

package de.niklasfauth.litewave.pages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import me.streib.janis.dbaufzug.pages.statistics.APIAvailibility;

import org.json.JSONException;

import de.niklasfauth.litewave.DatabaseConnection;
import de.niklasfauth.litewave.analyze.Analyze;
import de.niklasfauth.litewave.analyze.ConcAnalyze;
import de.niklasfauth.litewave.analyze.PeakLabel;
import de.niklasfauth.litewave.objects.ConcSpectraPlotValues;
import de.niklasfauth.litewave.objects.SpectraPlotValues;
import de.niklasfauth.litewave.pages.visualizing.ConcPie;
import de.niklasfauth.litewave.pages.visualizing.ConcSpectra;
import de.niklasfauth.litewave.pages.visualizing.ElementSpectra;

public class ReversePage extends Page {
	// private Availibility availStat = new Availibility();
	// private ElementSpectra rawSpectra = new ElementSpectra();
	private static SpectraPlotValues values;
	private APIAvailibility apiStat = new APIAvailibility();

	private boolean error = false;
	private boolean pieReady = false;

	private static String validElements = "Ac Ag Al Am Ar As At Au B Ba Be Bh Bi Bk Br C Ca Cd Ce Cf Cl Cm Cn Co Cr Cs Cu Db Ds Dy Er Es Eu F Fe Fl Fm Fr Ga Gd Ge H He Hf Hg Ho Hs I In Ir K Kr La Li Lr Lu Lv Md Mg Mn Mo Mt N Na Nb Nd Ne Ni No Np O Os P Pa Pb Pd Pm Po Pr Pt Pu Ra Rb Re Rf Rg Rh Rn Ru S Sb Sc Se Sg Si Sm Sn Sr Ta Tb Tc Te Th Ti Tl Tm U Uuo Uup Uus Uut V W Xe Y Yb Zn Zr";

	public ReversePage() throws IOException {
		super("Auswertung: Konzentration");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException, JSONException,
			SQLException {

		vars.put("availibility", new ConcSpectra());
		vars.put("resultSet", SpectraPlotValues.getResultString());
		if (error)
			vars.put("error", "Ungültige Eingabe!");
		// vars.put("apiavail", apiStat);
		if (pieReady) {
		vars.put("concpie", new ConcPie());}
		getDefaultTemplate().output(resp.getWriter(), vars);

		// req.getSession().setAttribute("plotdata", arg1);

	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws SQLException, IOException {

		// SpectraPlotValues.setConfig(Float.parseFloat(req.getParameter("offset")),
		// Float.parseFloat(req.getParameter("maxoffset")),
		// Integer.parseInt(req.getParameter("elepeak")),
		// Integer.parseInt(req.getParameter("optradio")),
		// Float.parseFloat(req.getParameter("coefficient")));

		String elements = req.getParameter("searchfor");
		elements = elements.replaceAll(" ", "");
		String[] parts = elements.split(";");
		error = false;
		for (int i = 0; i < parts.length; i++) {
			if (!validElements.contains(parts[i])) {
				error = true;
				resp.sendRedirect("/reverse");
				return;
			}

		}

		ConcSpectraPlotValues.setConfig(elements, Integer.parseInt(req.getParameter("numele")), Double.parseDouble(req.getParameter("ref")), Integer.parseInt(req.getParameter("offset")), Double.parseDouble(req.getParameter("scale")));
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			executor.submit(new ConcAnalyze()).get(20, TimeUnit.SECONDS);
			resp.sendRedirect("/reverse");
		} catch (InterruptedException | ExecutionException | TimeoutException e1) {
			resp.getWriter().print("Fehler: Timeout");
			e1.printStackTrace();
		} // Timeout of 10 minutes.
		executor.shutdown();
		pieReady = true;

	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

	public static LinkedList<Double> parseFile(String fileName)
			throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		LinkedList<Double> outputList = new LinkedList<Double>();
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
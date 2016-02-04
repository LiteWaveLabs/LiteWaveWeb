package de.niklasfauth.litewave.pages.visualizing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import me.streib.janis.dbaufzug.DatabaseConnection;

import org.json.JSONArray;

import de.niklasfauth.litewave.objects.SpectraPlotValues;

public class Spectra extends StatisticOutputable {

	public void output(PrintWriter out, Map<String, Object> vars) {
		LinkedList<Object> valueX = SpectraPlotValues.getRawPlot()[0];
		LinkedList<Object> valueY = SpectraPlotValues.getRawPlot()[1];

		// JSONArray rawIntensity = new JSONArray(valueY);

		JSONArray wavelenght = new JSONArray(valueX);
		JSONArray rawValue = new JSONArray(valueY);

		vars.put("peakList", SpectraPlotValues.getPeakList());
		vars.put("wavelenght", wavelenght.toString());
		vars.put("raw", rawValue.toString());
		vars.put("peak", SpectraPlotValues.getPeakPlot());
		vars.put("title", "Messung 1");
		getDefaultTemplate().output(out, vars);
	}

	public static JSONArray flipArray(JSONArray in) {
		JSONArray res = new JSONArray();
		for (int i = in.length() - 1; i >= 0; i--) {
			res.put(in.get(i));
		}
		return res;
	}

}

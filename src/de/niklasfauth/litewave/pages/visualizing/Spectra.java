package de.niklasfauth.litewave.pages.visualizing;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Map;

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

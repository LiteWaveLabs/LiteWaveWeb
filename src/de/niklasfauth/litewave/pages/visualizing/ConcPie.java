package de.niklasfauth.litewave.pages.visualizing;

import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import de.niklasfauth.litewave.objects.ConcSpectraPlotValues;

public class ConcPie extends StatisticOutputable {

	@Override
	public void output(PrintWriter out, Map<String, Object> vars) {
		String output = " ";
		Double value = 0.0;
		int summe = 0;
		for (String key : ConcSpectraPlotValues.getPieList().keySet()) {
			System.out.println(key);
			System.out.println(ConcSpectraPlotValues.getPieList().get(key));
			output += "{name: '" + key + "', y: ";
			value = ConcSpectraPlotValues.getPieList().get(key);
			summe += value;
			output += ConcSpectraPlotValues.getPieList().get(key); // print 1
																	// then 2
																	// finally 3
			output += "},";

		}
		vars.put("gesamt", ConcSpectraPlotValues.getRefPeak());

		vars.put("conclist", output);

		getDefaultTemplate().output(out, vars);

	}
}
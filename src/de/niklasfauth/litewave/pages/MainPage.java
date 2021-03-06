package de.niklasfauth.litewave.pages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.dbaufzug.Page;

import org.json.JSONException;

public class MainPage extends Page {

	public MainPage() {
		super("Main");

	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException, JSONException,
			SQLException {

		int up = 0;

		/*
		 * vars.put("type", fac.getClass().getSimpleName());
		 * 
		 * 
		 * 
		 * vars.put("percents", (facs / 100f) * up); vars.put("amount", facs);
		 */

		getDefaultTemplate().output(resp.getWriter(), vars);
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}

}

package de.niklasfauth.litewave.pages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.dbaufzug.Page;

import org.json.JSONException;
import org.json.JSONObject;

public class EastereggPage extends Page {

	public EastereggPage() {
		super("EastereggPage");
		
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException, JSONException,
			SQLException {
		getDefaultTemplate().output(resp.getWriter(), vars);
	}

	@Override
	public boolean needsTemplate() {
		return true;
	}
	

}

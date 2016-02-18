package de.niklasfauth.litewave.pages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.dbaufzug.Page;

import org.cacert.gigi.output.template.IterableDataset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class TestPage extends Page {

	public TestPage() {
		super("TestPage");
		
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp,
			Map<String, Object> vars) throws IOException, JSONException,
			SQLException {


		  JSONObject obj1 = new JSONObject();
		  obj1.put("spastWavelenght", parseFile("outputX.txt"));
		  obj1.put("spastData", parseFile("outputY.txt"));


		resp.setContentType("text/plain");
        resp.getWriter().print(obj1.toString());
	}

	@Override
	public boolean needsTemplate() {
		return false;
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

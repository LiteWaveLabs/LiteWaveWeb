package de.niklasfauth.litewave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

//Edited by Niklas Faut, but mostly coded by Janis Streib

public class LiteWaveWebConfigurator {
	private Properties p;
	private static LiteWaveWebConfigurator instance;

	public LiteWaveWebConfigurator(InputStream in) throws IOException,
			SQLException {
		this.p = new Properties();
		p.load(in);
		instance = this;
	}

	public int getPort() {
		return Integer.parseInt(p.getProperty("litewave.port"));
	}

	public String getHostName() {
		return p.getProperty("litewave.name");
	}

	public String getDB() {
		return p.getProperty("litewave.db");
	}

	public String getDBUser() {
		System.out.println( p.getProperty("litewave.db.user"));
		return p.getProperty("litewave.db.user");
	}

	public String getDBPW() {
		return p.getProperty("litewave.db.pw");
	}

	public String getJDBCDriver() {
		return p.getProperty("litewave.db.driver");
	}

	public static LiteWaveWebConfigurator getInstance() {
		return instance;
	}

	private void store() {
		File f = new File("conf/");
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File("conf/litewave.properties");
		try {
			p.store(new FileOutputStream(f), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isHSTSEnabled() {
		return Boolean.getBoolean(p.getProperty("litewave.hsts", "true"));
	}
}

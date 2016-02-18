package de.niklasfauth.litewave.analyze;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.niklasfauth.litewave.DatabaseConnection;

public class ElementFinder {

	public static ArrayList<PeakLabel> mySQLHandler(double wavelenght,
			int numberOfElements, double maxOffset, int orderBy) {

		ArrayList<PeakLabel> fArrayList = new ArrayList<PeakLabel>();

		try {
			String orderByString = null;

			if (orderBy == 2) {
				orderByString = "probability";
			}

			else if (orderBy == 1) {
				orderByString = "intensity";
			} else {
				System.out.println("Error: Sort methode not decleared");
			}

			PreparedStatement prep = DatabaseConnection.getInstance().prepare(
					"(SELECT * FROM asd ORDER BY abs(wavelength - ?) LIMIT ?) ORDER BY "
							+ orderByString + " DESC;");

			prep.setDouble(1, wavelenght);
			prep.setInt(2, numberOfElements);

			ResultSet rs = prep.executeQuery();

			while (rs.next()) {
				// Retrieve by column name
				int intensity = rs.getInt("intensity");
				String element = rs.getString("element");
				double actualWavelength = rs.getFloat("wavelength");
				long ASDprobability = rs.getLong("probability");
				PeakLabel newLabel = new PeakLabel(intensity, element,
						actualWavelength, ASDprobability);
				if (actualWavelength < wavelenght + maxOffset
						&& actualWavelength > wavelenght - maxOffset) {
					fArrayList.add(newLabel);
				}
			}

			// Clean-up environment
			rs.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}
		return fArrayList;
	}
}

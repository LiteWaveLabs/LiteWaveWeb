package de.niklasfauth.litewave.analyze;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.niklasfauth.litewave.DatabaseConnection;
import de.niklasfauth.litewave.objects.SpectraPlotValues;

public class ElementFinder {

	public static ArrayList<PeakLabel> mySQLHandler(double wavelenght,
			int numberOfElements, double maxOffset, int orderBy, float coefficient) {

		ArrayList<PeakLabel> fArrayList = new ArrayList<PeakLabel>();

		try {
			PreparedStatement prep = null;
			if (orderBy == 2) {
				prep = DatabaseConnection.getInstance().prepare(
						"(SELECT * FROM asd ORDER BY abs(wavelength - ?) LIMIT ?) ORDER BY probability DESC;");
			}

			else if (orderBy == 1) {
				prep = DatabaseConnection.getInstance().prepare(
						"(SELECT * FROM asd ORDER BY abs(wavelength - ?) LIMIT ?) ORDER BY intensity DESC;");
			} 
			
			else if (orderBy == 3) {
				prep = DatabaseConnection.getInstance().prepare(
						"SELECT * FROM asd ORDER BY ((" + maxOffset + " - abs(wavelength - ?)) * POWER(intensity, " + coefficient +  ")) DESC LIMIT ?;");
			} 
			
			else {
				System.out.println("Error: Sort methode not decleared");
			}

			System.out.println(prep);

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

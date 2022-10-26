package com.booking;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import com.helper.Validatation;

public class Display {
	static Statement stmt;
	static ResultSet rs;
	static String email = null;
	static Properties prop = null;

	// display method
	public void DisplayTicketDetails(Connection conn) {
		Properties prop = new Properties();
		FileInputStream ip;
		try {
			ip = new FileInputStream("C:\\projectM\\TicketBook\\src\\resource\\Config.properties");
			prop.load(ip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Validatation v = new Validatation();
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String sql = "SELECT email FROM "+prop.getProperty("UserDataTable")+" WHERE LoginStatus = true";
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				email = rs.getString("email");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		System.out.println(email);

//----------------------------------------------------------------------------------------------------
		while (true) {
			if (v.emailCheckingValidation(email, conn)) {
				break;
			} else {
				System.out.println("***********DATA NOT FOUND***********");
				break;
			}
		}

		try {
			stmt = conn.createStatement();
			String sql1 = "SELECT * FROM "+prop.getProperty("TicketBookingTable")+" WHERE email ='" + email + "'";
			rs = stmt.executeQuery(sql1);
			while (rs.next()) {
				String registrationNo = rs.getString("regNo");
				String username = rs.getString("name");
				int userage = rs.getInt("age");
				int seatnum = rs.getInt("seatNo");

				// print the results
				System.out.println("********BOOKED TICKETS DETAILS********");
				System.out.format("REGISTER NUMBER   : " + "%s\n", registrationNo);
				System.out.format("SEAT NUMBER       : " + "%s\n", seatnum);
				System.out.format("PASSANGER NAME    : " + "%s\n", username);
				System.out.format("AGE               : " + "%s\n", userage);
				System.out.println();
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

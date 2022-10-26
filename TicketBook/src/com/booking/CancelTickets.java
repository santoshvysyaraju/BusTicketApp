package com.booking;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Scanner;

import com.helper.Validatation;

public class CancelTickets {
	static Statement stmt;
	static ResultSet rs;
	static String email = null;
	static Properties prop = null;
	static String regno = null;

	public void cancelTickets(Connection conn) {
		
		Properties prop = new Properties();
		FileInputStream ip;
		try {
			ip = new FileInputStream("C:\\projectM\\TicketBook\\src\\resource\\Config.properties");
			prop.load(ip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scanner s = new Scanner(System.in);
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

				System.out.println(
						"---------------------------------------------------------------------------------------------------------------");

				System.out.println("WHICH ONE YOU WANT TO CANCEL ?");
				int rn = 0;
				while (rn < 3) {
					System.out.println("ENTER REGISTER NUMBER ");
					regno = s.next();
					if (v.registerNumberValidation(regno, conn) && (regno.length() >= 10)) {
						break;
					} else {
						System.out.println("INVALID REGISTER NUMBER");
					}
					if (rn == 3) {
						System.out.println("TRY AGAIN AFTER 30 MINUTES...!");
						System.exit(rn);
					}
				}

				// getting hrs from register number and caluculating difference between register
				// time and present time.
				String hrs = regno.substring(6, 8);
				int i = Integer.parseInt(hrs);
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH");
				LocalDateTime now = LocalDateTime.now();
				String cancelDth = dtf.format(now);
				int j = Integer.parseInt(cancelDth);
				int deff = j - i;

				int tn = Integer.parseInt(regno.split("_")[1]);

				if (deff <= 12) {

					String deleteQuery = "delete from "+prop.getProperty("TicketBookingTable")+" where regNo = ?";
					PreparedStatement ps;
					try {
						ps = conn.prepareStatement(deleteQuery);
						ps.setString(1, regno);
						ps.executeUpdate();
						System.out.println("the regestraion no " + regno + " ticket is cancelled");

						PreparedStatement ps1 = conn.prepareStatement(
								"UPDATE "+prop.getProperty("DisplaySeatTable")+" SET status = 0 WHERE seatno = " + tn + ";");
						ps1.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("YOU CAN'T CANCEL TICKET...!");
				}
				System.out.println(
						"---------------------------------------------------------------------------------------------------------------");
				break;

			} else {
				System.out.println("***********DATA NOT FOUND***********");
				break;
			}
		}

	}
}

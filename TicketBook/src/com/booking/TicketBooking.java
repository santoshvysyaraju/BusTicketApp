package com.booking;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.helper.Validatation;

public class TicketBooking {
	static Statement stmt;
	static ResultSet rs;
	static int ticketNo = 0;
	static String userName = "";
	static int age = 0;
	static String email = null;
	static Properties prop = null;

	Scanner s = new Scanner(System.in);

	// Booking Ticket method
	public void BookingTicket(Connection conn) {
		
		
		Properties prop = new Properties();
		FileInputStream ip;
		try {
			ip = new FileInputStream("C:\\projectM\\TicketBook\\src\\resource\\Config.properties");
			prop.load(ip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// getting available tickets
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("select * from "+prop.getProperty("DisplaySeatTable")+" where status = ?");
			ps.setBoolean(1, false);
			ResultSet rs = ps.executeQuery();
			System.out.println("AVAILABLE SEAT NUMBERS");
			while (rs.next()) {
				System.out.print(rs.getString("seatNo") + " ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println();

		// validating ticket number
		Validatation v = new Validatation();

		// getting seat number
		int tr = 0;
		while (tr < 3) {
			System.out.println("ENTER SEAT NUMBER ");
			String ticketNum = s.nextLine();
			try {
				ticketNo = Integer.parseInt(ticketNum);
				if (v.seatNumberValidation(ticketNo, conn)) {
					break;
				} else {
					System.out.println("PLEASE ENTER AVAILABLE SEAT NUMBER");
					tr++;
				}
			} catch (NumberFormatException e) {
				System.out.println("PLEASE ENTER AVAILABLE SEAT NUMBER");
				tr++;
			}
			if (tr == 3) {
				System.out.println("TRY AGAIN AFTER SOME MINUTES...!");
				System.exit(tr);
			}
		}

		// getting name

		int un = 0;

		while (un < 3) {
			System.out.println("ENTER YOUR NAME :");
			userName = s.nextLine();
			if (v.nameValidation(userName)) {
				break;
			} else {
				System.out.println("PLEASE ENTER CORRECT NAME");
				un++;
			}
			if (un == 3) {
				System.out.println("TRY AGAIN AFTER SOME MINUTES...!");
				System.exit(un);
			}

		}
		// getting age
		int tt = 0;
		while (tt < 3) {
			System.out.println("ENTER  YOUR AGE :");
			String ageS = s.nextLine();
			try {
				age = Integer.parseInt(ageS);
				if (v.ageValidatation(age)) {
					break;
				} else {
					System.out.println("PLEASE ENTER CORRECT AGE");
					tt++;
				}
			} catch (NumberFormatException e) {
				System.out.println("PLEASE ENTER CORRECT AGE");
				tt++;
			}
			if (tt == 3) {
				System.out.println("TRY AGAIN AFTER SOME MINUTES...!");
				System.exit(tt);
			}
		}

		// Getting email from userdb beacause it is similar to every ticket in that user
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
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Generating unique Register Number
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		String regNum = dtf.format(now);
		String regNo = regNum + "_" + ticketNo;
		// System.out.println(regNo);
		

		PreparedStatement ps2;
		try {
			ps2 = conn.prepareStatement(
					"INSERT Into "+prop.getProperty("TicketBookingTable")+"(`regNo`,`name`,`age`,`seatNo`,`email`) VALUES(?,?,?,?,?)");
			ps2.setString(1, regNo);
			ps2.setString(2, userName);
			ps2.setInt(3, age);
			ps2.setInt(4, ticketNo);
			ps2.setString(5, email);
			ps2.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		PreparedStatement ps1;
		try {
			ps1 = conn.prepareStatement("UPDATE "+prop.getProperty("DisplaySeatTable")+" SET status = True WHERE seatno = ?;");
			ps1.setInt(1, ticketNo);
			ps1.executeLargeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(
				"---------------------------------------------------------------------------------------------------------------");
		System.out.println("TICKET CONFIRMED WITH SEAT NUMBER " + ticketNo + " AND YOUR TICKETS NUMBER IS " + regNo);
		System.out.println();
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------");

	}

}

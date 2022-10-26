package com.helper;

import java.util.regex.*;

import com.booking.TicketBooking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//creating methods for validating register number and ticket number

public class Validatation {
	Statement stmt;
	ResultSet rs;

	public boolean registerNumberValidation(String regNo, Connection conn) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("SELECT * FROM ticketdatabase.ticketbooking WHERE regNo =" + regNo);
			ps.executeQuery();
		} catch (Exception e) {
			return true;
		}
		return false;
	}

	public boolean seatNumberValidation(int ticketNo, Connection conn) {

		// boolean tv = Integer.parseInt(ticketNo);

		if (ticketNo < 1 || ticketNo > 20) {
			return false;
		} else {
			try {
				String sql = "select status from ticketdatabase.displayseat where seatNo =" + ticketNo;
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);

				rs.next();
				boolean st = rs.getBoolean(1);
				if (st == false) {
					return true;
				} else {
					return false;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	public static String getConsoleInput() {
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

	public boolean AskYesOrNoValidation() {
		String YesOrNo = null;
		boolean validInput = false;
		while (!validInput) {
			YesOrNo = getConsoleInput();
			validInput = YesOrNo.toUpperCase().equals("Y") || YesOrNo.toUpperCase().equals("N");
			if (!validInput) {
				System.out.println("INVALID INPUT PLEASE ENTER  'Y' or 'N'");
			}
		}
		return YesOrNo.toUpperCase().equals("Y");
	}


	public int phoneNumberValidation(long phoneNo) {
		int count = 0;
		while (phoneNo != 0) {
			phoneNo = phoneNo / 10;
			count = count + 1;
		}
		return count;
	}

	public boolean isValidEmail(String email) {
		String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; // @ .com
		return email.matches(regex);
	}

	public boolean emailCheckingValidation(String email, Connection conn) {
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ticketdatabase.ticketbooking WHERE email ='" + email + "'");
			if (rs.next() == true) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean emailValidationOfUser(String email, Connection conn) {
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ticketdatabase.userdb WHERE email ='" + email + "'");
			if (rs.next() == true) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean LoginTimeValidation(String email, Connection conn) throws ParseException {
		// table logout time
		String logOutTime = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String sql1 = "SELECT flag FROM ticketdatabase.userdb WHERE email ='" + email + "'";
		try {
			rs = stmt.executeQuery(sql1);
			rs.next();
			logOutTime = rs.getString(1);
			// System.out.println(logOutTime);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// handle exception for null result set .
			e.printStackTrace();
		}

		// login time
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String loginTime = dtf.format(now);
		// System.out.println(loginTime);
		// calculating defference
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date1 = format.parse(logOutTime);
		Date date2 = format.parse(loginTime);
		long difference = date2.getTime() - date1.getTime();
		if (difference > 180000) {
			return true;
		} else {
			return false;
		}
	}

	public boolean ageValidatation(int n) {
		if (n > 0 || n < 101) {
			return true;
		} else {
			return false;
		}
	}

	public void tickets(int n, Connection conn) {
		TicketBooking tb[] = new TicketBooking[n];
		for (int i = 0; i < tb.length; i++) {
			tb[i] = new TicketBooking();
			tb[i].BookingTicket(conn);
		}
	}


	public boolean nameValidation(String name) {
		Pattern p = Pattern.compile("^[ A-Za-z]+$");
		Matcher m = p.matcher(name);
		return m.matches();
	}

	public boolean choiceValidation(int n) {
		if ((n >= 1) || (n <= 4)) {
			return true;
		} else {
			return false;
		}
	}
}

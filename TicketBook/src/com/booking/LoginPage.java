package com.booking;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

import com.helper.Validatation;

public class LoginPage {
	static String email = null;
	static String password;
	static Statement stmt;
	static ResultSet rs;
	static String ps;
	static Properties prop = null;
	Scanner s = new Scanner(System.in);

	public void login(Connection conn) {
		
		Properties prop = new Properties();
		FileInputStream ip;
		try {
			ip = new FileInputStream("C:\\projectM\\TicketBook\\src\\resource\\Config.properties");
			prop.load(ip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Validatation v = new Validatation();
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------");

		int j = 0;
		while (j < 3) {
			System.out.println("ENTER YOUR EMAIL:");
			// if you recive null from database handle that expcetion and
			email = s.next();

			if ((v.isValidEmail(email)) && (v.emailValidationOfUser(email, conn))) {
				try {
					if (v.LoginTimeValidation(email, conn)) {
						break;
					} else {
						System.out.println("TRY AGAIN AFTER 3 MINUTES...!");
						System.exit(0);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("ENTER VALID EMAIL");
				j += 1;
			}
			if (j == 3) {
				System.out.println("TRY AGAIN AFTER 3 MINUTES...!");
				System.exit(j);
			}
		}

		// --------------------------------------------------------------------------------------------

		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String sql = "SELECT password FROM "+prop.getProperty("UserDataTable")+" WHERE email ='" + email + "'";
		try {
			rs = stmt.executeQuery(sql);
			rs.next();
			ps = rs.getString(1);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// handle exception for null result set .
			e.printStackTrace();
		}
		int pw = 0;
		while (pw < 3) {
			System.out.println("ENTER  YOUR PASSWORD :");
			password = s.next();
			if (Objects.equals(ps, password)) {
				System.out.println("LOGIN SUCCESSFULLY..!");
				break;
			} else {
				System.out.println("PASSWORD WRONG");
				pw++;
			}
			if (pw == 3) {
				System.out.println("TRY AGAIN AFTER 3 MINUTES...!");
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				String secOfWrongLogout = dtf.format(now);
				// System.out.println(secOfWrongLogout);

				PreparedStatement ps2;
				try {
					ps2 = conn.prepareStatement("UPDATE "+prop.getProperty("UserDataTable")+" SET flag = ? WHERE email = ?;");
					ps2.setString(1, secOfWrongLogout);
					ps2.setString(2, email);
					ps2.executeLargeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				System.exit(pw);

			}
		}
		PreparedStatement ps1;
		try {
			ps1 = conn.prepareStatement("UPDATE "+prop.getProperty("UserDataTable")+" SET LoginStatus = true WHERE email = ?;");
			ps1.setString(1, email);
			ps1.executeLargeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(
				"---------------------------------------------------------------------------------------------------------------");
	}
}

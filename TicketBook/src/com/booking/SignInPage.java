package com.booking;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

import com.helper.Validatation;

public class SignInPage {
	static String name = "";
	static Long phoneNo = null;
	static String email = null;
	static String password = null;
	static String time;
	static Properties prop = null;
	Scanner s = new Scanner(System.in);

	public void signIn(Connection conn) {
		
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
		System.out.println("SIGN IN");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------");
		int un = 0;
		while (un < 3) {
			System.out.println("ENTER YOUR NAME :");
			name = s.nextLine();
			if (v.nameValidation(name)) {
				break;
			} else {
				System.out.println("PLEASE ENTER CORRECT NAME");
				un++;
			}
			if (un == 3) {
				System.out.println("TRY AGAIN AFTER SOME TIMES...!");
				System.exit(un);
			}

		}

		int i = 0;
		while (i < 3) {
			System.out.println("ENTER YOUR PHONE NUMBER :");
			String phoneNum = s.next();
			try {
				phoneNo = Long.parseLong(phoneNum);
				if (v.phoneNumberValidation(phoneNo) == 10) {
					break;
				} else {
					System.out.println("ENTER 10 DIGIT CORRECT NUMBER");
					i += 1;
				}
			} catch (NumberFormatException e) {
				System.out.println("PLEASE ENTER CORRECT NUMBER");
				i += 1;
			}
			if (i == 3) {
				System.out.println("TRY AGAIN AFTER SOME TIME...!");
				System.exit(i);
			}
		}

		int j = 0;
		while (j < 3) {
			System.out.println("ENTER YOUR EMAIL:");
			email = s.next();
			if (v.isValidEmail(email)) {
				break;
			} else {
				System.out.println("ENTER VALID EMAIL");
				j += 1;
			}
			if (j == 3) {
				System.out.println("TRY AGAIN AFTER SOME TIME...!");
				System.exit(j);
			}
		}
		int k = 0;
		while (k < 3) {
			System.out.println("ENTER YOUR PASSWORD :");
			String p1 = s.next();
			System.out.println("CONFIRM YOUR PASSWORD:");
			String p2 = s.next();
			if (Objects.equals(p1, p2)) {
				password = p2;
				break;
			} else {
				System.out.println("PASSWORD MISMATCHED PLEASE ENTER CORRECT PASSWORD");
				k += 1;
			}
			if (k == 3) {
				System.out.println("TRY AGAIN AFTER SOMETIME...!");
				System.exit(k);
			}
		}
		time = "00:00:00";

		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(
					"INSERT Into "+prop.getProperty("UserDataTable")+"(`name`,`email`,`phoneNo`,`password`,`flag`) VALUES(?,?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2, email);
			ps.setLong(3, phoneNo);
			ps.setString(4, password);
			ps.setString(5, time);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("SUCCESSFULLY SIGN IN");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------");
	}

}

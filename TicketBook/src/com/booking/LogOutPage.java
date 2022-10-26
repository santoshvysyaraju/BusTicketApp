package com.booking;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class LogOutPage {
	static Statement stmt;
	static ResultSet rs;
	static Properties prop = null;
	public void logout(Connection conn) {
		
		Properties prop = new Properties();
		FileInputStream ip;
		try {
			ip = new FileInputStream("C:\\projectM\\TicketBook\\src\\resource\\Config.properties");
			prop.load(ip);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String sql = "SELECT email FROM "+prop.getProperty("UserDataTable")+" WHERE LoginStatus = true";
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String email1 = rs.getString("email");

				PreparedStatement ps1;
				try {
					ps1 = conn.prepareStatement("UPDATE ticketdatabase.userdb SET LoginStatus = false WHERE email= ?;");
					ps1.setString(1, email1);
					ps1.executeLargeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}

package com.Db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

//Db connection implementaion

public class DbImplementation implements DbConnection {
	static Connection conn = null;
	static Properties prop = null;
	static Statement stmt;
//	static boolean dbConnection = false;

	public Connection con() {
		Properties prop = new Properties();
		FileInputStream ip;
		try {
			ip = new FileInputStream("C:\\projectM\\TicketBook\\src\\resource\\Config.properties");
			prop.load(ip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Class.forName(prop.getProperty("DataBaseDriverClass"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(prop.getProperty("DataBaseUrl"), prop.getProperty("DataBaseUserName"),
					prop.getProperty("DataBaseUserPassword"));
//			dbConnection = true;
		} catch (SQLException e) {
//			e.printStackTrace();
//			dbConnection = false;
		}
		return conn;

	};
}
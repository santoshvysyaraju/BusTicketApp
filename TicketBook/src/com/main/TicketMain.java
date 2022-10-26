package com.main;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.Db.*;
import com.booking.*;
import com.helper.Validatation;

public class TicketMain {
	static int num = 0;

	public static void main(String[] args){
		

		// Db method calling
		Connection conn = null;
		DbConnection db = new DbImplementation();
		conn = db.con();
		Scanner s = new Scanner(System.in);

		// choosing

		System.out.println("\n**********TICKET BOOKING APPLICATION**********");
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------");
		System.out.println("DON'T HAVE AN ACCOUNT ? Y/N");
		Validatation v = new Validatation();
		boolean Yrn = v.AskYesOrNoValidation();

		SignInPage sn = new SignInPage();
		LoginPage lp = new LoginPage();

		if (Yrn == true) {
			sn.signIn(conn);
			System.out.println("LOGIN NOW");
			lp.login(conn);
		} else {
			lp.login(conn);
		}

		do {
			System.out.println("1. TICKET BOOKING\n2. CANCEL TICKET\n3. DISPLAY DETAILS\n4. EXIT");

			int dts = 0;
			while (dts < 3) {
				System.out.println("ENTER YOUR CHOICE : ");
				String nums = s.next();
				try {
					num = Integer.parseInt(nums);
					if (v.choiceValidation(num)) {
						break;
					} else {
						System.out.println("PLEASE ENTER CORRECT CHOICE");
						dts++;
					}
				} catch (NumberFormatException e) {
					System.out.println("PLEASE ENTER CORRECT CHOICE");
					dts++;
				}
//				if(dts == 3) {
//					System.out.println("TRY AGAIN AFTER SOME MINUTES...!");
//					System.exit(dts);
//				}
			}

			switch (num) {

			// Booking implementaion
			case 1:
				// TicketBooking tb = new TicketBooking();
				while (true) {
					System.out.println("HOW MANY TICKETS YOU WANT ?");
					int n = s.nextInt();
					// Getting available seat numbers.
					int m = 0;
					try {
						PreparedStatement ps1;
						ps1 = conn.prepareStatement("select * from ticketdatabase.displayseat where status = ?");
						ps1.setBoolean(1, false);
						ResultSet rs = ps1.executeQuery();

						while (rs.next()) {
							m++;
							rs.getInt(1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (n <= m) {
						v.tickets(n, conn);
						break;
					} else {
						System.out.println("YOU CAN'T BOOK MORE THEN " + m + " TICKETS.");
					}
				}
				break;
			case 2:
				// cancelation of ticket booking implementaion

				CancelTickets ct = new CancelTickets();
				ct.cancelTickets(conn);
				break;
			case 3:
				// Display user details implementaion
				Display dt = new Display();
				dt.DisplayTicketDetails(conn);
				break;
			case 4:
				LogOutPage lop = new LogOutPage();
				lop.logout(conn);
				System.out.println("USER LOGGED OUT");
				System.out.println("**THANK YOU***");
				break;
			}
		} while (num != 4);
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		s.close();
	}
}

package com.setvect.bokslmoney.test.migraion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class SqlLiteConnection {
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		connect();
	}

	/**
	 * Connect to a sample database
	 */
	public static void connect() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String url = "jdbc:sqlite:./temp/setvect.db";
			conn = DriverManager.getConnection(url);
			ps = conn.prepareStatement("select  * from t_account");
			rs = ps.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getString("a_name"));
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		System.out.println("끝.");
	}

}
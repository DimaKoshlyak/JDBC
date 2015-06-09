package goodsMySqlJDBC;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/goods";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "r00t";
	
	private static Connection getDBConnection() {
		Connection dbConnection = null;

		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
					DB_PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return dbConnection;
	}

	public static void main(String[] args) throws SQLException, IOException {
		Scanner scanner = new Scanner(System.in);
		String choice;
		boolean done = false;

		Connection conn = getDBConnection();
		if (conn == null) {
			System.out.println("Error creating connection!");
			return;
		}

		while (!done) {
			System.out
					.println("What do you want to do? \r 1 - add Products \r 2 - add Clients \r 3 - show Clients list \r 4 - show Products list \r q - quit");
			choice = scanner.nextLine();
			switch (choice) {
			case "1":
				try {
					Statement st = conn.createStatement();
					try {
						st.executeUpdate("INSERT INTO Goods (name, price) VALUES('"
								+ input("Enter name of product")
								+ "', "
								+ Integer.valueOf(input("Enter price")) + ")");
					} finally {
						if (st != null)
							st.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "2":
				try {
					Statement st = conn.createStatement();
					try {
						st.executeUpdate("INSERT INTO Clients (name, phone) VALUES('"
								+ input("Enter name of client")
								+ "', "
								+ input("Enter phone #") + ")");
					} finally {
						if (st != null)
							st.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "3":
				clientsOut();
				break;
			case "4":
				productsOut();
				break;
			case "q":
				done = true;
			}
		}
	}

	public static String input(String message) {
		String s;
		Scanner scanner = new Scanner(System.in);
		System.out.println(message);
		s = scanner.nextLine();
		return s;
	}

	public static void clientsOut() throws SQLException, IOException {
		Connection conn = getDBConnection();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM Clients");
		dispResultSet(rs);
	}

	public static void productsOut() throws SQLException, IOException {
		Connection conn = getDBConnection();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM Goods");
		dispResultSet(rs);
	}

	private static void dispResultSet(ResultSet rs) throws SQLException {
		try {
			ResultSetMetaData md = rs.getMetaData();

			for (int i = 1; i <= md.getColumnCount(); i++)
				System.out.print(md.getColumnName(i) + "\t\t");
			System.out.println();

			while (rs.next()) {
				for (int i = 1; i <= md.getColumnCount(); i++) {
					System.out.print(rs.getString(i) + "\t\t");
				}
				System.out.println();
			}
		} finally {
			rs.close();
		}
	}
}

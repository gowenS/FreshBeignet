package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBconnection {	
	public static Connection getConnectionToDatabase() {
		Connection connection = null;
		Properties props = new Properties();
		String database = null;
		String username = null;
		String password = null;
		try {
			if(System.getProperty("os.name").toLowerCase().charAt(0) == 'w'){
				//Windows
				props.load(new FileInputStream(new File("c:\\Users\\Sean\\db_dumps\\credentials.properties")));
			} else {
				//Linux
				props.load(new FileInputStream(new File("/usr/db_dumps/credentials.properties")));
			}	
			database = props.getProperty("database");
			username = props.getProperty("username");
			password = props.getProperty("password");
			
			// load the driver class
			Class.forName("com.mysql.jdbc.Driver");

			// get hold of the DriverManager
			connection = DriverManager.getConnection(database, username, password);			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}

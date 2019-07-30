package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class HostDao {	
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	// Create game instance in MySQL
	public String createGame(String host_name, HttpSession session) {
		String gameName = "";		
		try {
			// Get the connection for the database
			Connection connection = DBconnection.getConnectionToDatabase();
			
			// Create name for game
			gameName = createGameName(connection);

			// Write the select query
			sql = "insert into onitama_games(game_name, red) values(?, ?)";

			// Set parameters with PreparedStatement
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameName);
			statement.setString(2, host_name);
			statement.executeUpdate();
		
		} catch (SQLException exception) {
			exception.printStackTrace();
		}		
		return gameName;
	}		
	
	// Start game
	public void initializeGameHost(String gameName) {
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			sql = "update games set game_state = 1 where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameName);
			statement.executeUpdate();
		} catch(SQLException exception){
			exception.printStackTrace();
		}		
	}
	
	
	
	// Create name for game and perform checks to ensure name is valid.
	private String createGameName(Connection connection) {		
		String gameName = generateString();		
		try {			
			sql = "select * from excluded_words where word = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameName);
			set = statement.executeQuery();			
			//write code to check for pre-existing games with same name, re-generate name if found
			while(set.next()) {
				System.out.println("Found an excluded word: " + gameName );
				gameName = generateString();
				statement = connection.prepareStatement(sql);
				statement.setString(1, gameName);
				set = statement.executeQuery();
			}
			return gameName;
		} catch(SQLException exception) {
			exception.printStackTrace();
		}		
		return gameName;
	}
	
	// Generate random string of length 4 to be used as game name
	private String generateString() {
		Random rand = new Random();
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < 4; i++) {
			out.append((char)(rand.nextInt(26)+65));
		}
		return out.toString();
	}
	
}

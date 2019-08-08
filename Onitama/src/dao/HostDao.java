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
			String board_pos = "bblbb"
					+ "nnnnn"
					+ "nnnnn"
					+ "nnnnn"
					+ "rrjrr";
			String highlight = "00000"
					+ "00000"
					+ "00000"
					+ "00000"
					+ "00000";
			String selectable = "00000"
					+ "00000"
					+ "00000"
					+ "00000"
					+ "00000";
					
			// Write the select query
			sql = "insert into onitama_games(game_name, red, board_pos, highlight, selectable, blue, ropt1, ropt2, bopt1, bopt2, rnext, bnext, rplay, bplay, game_state) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			// Set parameters with PreparedStatement
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameName);
			statement.setString(2, host_name);
			statement.setString(3, board_pos);
			statement.setString(4, highlight);
			statement.setString(5, selectable);
			statement.setString(6, "");
			statement.setInt(7, 0);
			statement.setInt(8, 0);
			statement.setInt(9, 0);
			statement.setInt(10, 0);
			statement.setInt(11, 0);
			statement.setInt(12, 0);
			statement.setInt(13, 0);
			statement.setInt(14, 0);
			statement.setInt(15, 0);
			statement.executeUpdate();
		
		} catch (SQLException exception) {
			exception.printStackTrace();
		}		
		return gameName;
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

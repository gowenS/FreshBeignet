package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import classes.WordList;


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
								
			// Write the insert command
			sql = "insert into codenames_games(game_name,round_num,spy_red,spy_blue,turn,words,revealed,clue,clue_number) values(?,?,?,?,?,?,?,?,?)";
			
			// Set parameters with PreparedStatement
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameName);
			statement.setInt(2, 0);
			statement.setInt(3, 0);
			statement.setInt(4, 0);
			statement.setString(5, "");
			statement.setString(6, "");
			statement.setString(7, "");
			statement.setString(8, "");
			statement.setInt(9, 0);
			statement.executeUpdate();
			session.setAttribute("gameName", gameName);
			
			// Create table to hold player names
			statement = connection.prepareStatement(makeNamesTableString(gameName));
			statement.executeUpdate();
			
			sql = "insert into " + gameName + "_players(player_name,player_color) values(?,?)";
			statement = connection.prepareStatement(sql);
			statement.setString(1, host_name);
			statement.setString(2, "n");
			statement.executeUpdate();
			session.setAttribute("playerName", host_name);
			session.setAttribute("playerColor", "n");
			session.setAttribute("redTeam", "");
			session.setAttribute("blueTeam", "");
			session.setAttribute("noTeam", "");
			session.setAttribute("gameState", 0);
			
			sql = "select * from " + gameName +"_players where player_name = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, host_name);
			set = statement.executeQuery();
			set.next();
			session.setAttribute("my_ID", set.getInt("player_id"));
			
			// Create table to store word bank for this game
			WordList initialWordBank = new WordList(); 
			sql = "insert into codenames_wordbank(game_name, words_remaining) values (?,?)";
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameName);
			statement.setString(2, initialWordBank.getInitialWordList());
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
	
	// Build string to be used as SQL statement for making table in MySQL database to represent the game instance
	private String makeNamesTableString(String name) {
		StringBuilder out = new StringBuilder();
		out.append("create table ");
		out.append(name + "_players");
		out.append("(player_id integer not null auto_increment, player_name varchar(12), player_color varchar(1), primary key (player_id))");
		return out.toString();
	}
	
}

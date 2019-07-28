package net.freshbeig.dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Random;
import java.util.Base64.Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class HostDao {	
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	// Create game instance in MySQL
	public String createGame(int playerCount, HttpSession session) {
		String gameName = "";		
		try {
			// Get the connection for the database
			Connection connection = DBconnection.getConnectionToDatabase();
			
			// Create name for game
			gameName = createGameName(connection);

			// Write the select query
			sql = "insert into games(game_name, tot_players, players_in_lobby, game_state) values(?, ? , 0, 0)";

			// Set parameters with PreparedStatement
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameName);
			statement.setString(2, session.getAttribute("numPlayers").toString());

			// Execute the update and check whether user exists
			statement.executeUpdate();			
			sql = makeGameTableString(gameName,(int) session.getAttribute("numPlayers"));			
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();			
		} catch (SQLException exception) {
			exception.printStackTrace();
		}		
		return gameName;
	}	
	
	// Find which players have submitted either a description or a drawing in a given turn
	public LinkedList<Boolean> getPlayersSubmitted(HttpSession session){
		LinkedList<Boolean> playersSubmitted = new LinkedList<>();
		String gameName = (String) session.getAttribute("gameName");
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			sql = "select game_state from games where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1,gameName);
			set = statement.executeQuery();
			set.next();
			int gameState = set.getInt("game_state");
			String turnRep = "turn" + gameState;
			sql = "select " + turnRep + " from " + gameName;
			statement = connection.prepareStatement(sql);			
			set = statement.executeQuery();	
			boolean allSubmitted = true;
			while(set.next()) {
				if(set.getString(turnRep)!=null) {
					playersSubmitted.add(true);
				} else {
					playersSubmitted.add(false);
					allSubmitted = false;
				}
			}
			if(allSubmitted) {
				sql = "update games set game_state = " +(gameState+1) + " where game_name like ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1,gameName);
				statement.executeUpdate();
				session.setAttribute("game_state", gameState +1);
				playersSubmitted = new LinkedList<>();
				for(int i = 0; i<((int)session.getAttribute("numPlayers")); i++) {
					playersSubmitted.add(false);
				}
			}			
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		return playersSubmitted;	
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
	
	// Get a list of all players joined in a single game
	public LinkedList<String> getJoinedPlayers(HttpSession session) {
		LinkedList<String> playersJoined = new LinkedList<>();
		try {			
			Connection connection = DBconnection.getConnectionToDatabase();
			String gameName = (String) session.getAttribute("gameName");
			sql = "select player_name from "+ gameName ;
			statement = connection.prepareStatement(sql);			
			set = statement.executeQuery();
			while(set.next()) {
				playersJoined.add(set.getString("player_name"));
			}			
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return playersJoined;
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
	private String makeGameTableString(String name, int numPlayers) {
		StringBuilder out = new StringBuilder();
		out.append("create table ");
		out.append(name);
		out.append("(player_id integer, player_name varchar(12)");
		for(int i = 1; i <= numPlayers; i++) {
			out.append(", turn");
			out.append(Integer.toString(i));
			if(i%2 == 0) {
				out.append(" varchar(300)"); // text description of drawing
			} else {
				out.append(" varchar(100)"); // drawing path
			}
		}
		out.append(")");
		return out.toString();
	}
	
	public int[] getPlayerNumLims() {
		int[] out = new int[2];
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			sql = "select * from player_lims where game_name = 'Descriptionary'";
			statement = connection.prepareStatement(sql);
			set = statement.executeQuery();
			set.next();
			out[0] = set.getInt("min_players");
			out[1] = set.getInt("max_players");			
		} catch(SQLException exception) {
			exception.printStackTrace();
		}		
		return out;
	}
}

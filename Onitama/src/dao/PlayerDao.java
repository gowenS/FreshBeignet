package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import classes.MoveCard;

public class PlayerDao {
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	// Player joining game 
	public int joinGame(String gameNameAttempt, String playerNameAttempt, HttpSession session){
		int code = 0;
		try {			
			Connection connection = DBconnection.getConnectionToDatabase();			
			sql = "select * from onitama_games where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameNameAttempt);
			set = statement.executeQuery();
			if (set.next()) {
				code = 1; // success
				String blue_exist = set.getString("blue");
				if(blue_exist.length() > 0) return 3; // lobby full error code
				sql = "update onitama_games set blue = ? where game_name = ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, playerNameAttempt);
				statement.setString(2, gameNameAttempt);
				statement.executeUpdate();
				session.setAttribute("game_name", gameNameAttempt);
				getGameState(session);
				buildGameDeck(connection, session, gameNameAttempt);
				session.setAttribute("player_color", "blue");
				incrementGS(connection, session);
			} else {
				return 2; // game doesn't exist error
			}			
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		return code;		
	}
	
	// Handles button press
	public void buttonPress(String button,HttpSession session) {
		Connection connection = DBconnection.getConnectionToDatabase();
		getGameState(session);
		if (button.contains("opt") || button.contains("play")) {
			chooseMove(button,session,connection);
		} else {
			
		}
		incrementGS(connection,session);
		setGameState(connection,session);
	}
	
	// Move Card is pressed
	private void chooseMove(String button,HttpSession session,Connection connection) {
		// TODO FIX THIS METHOD. IT GETS RID OF CARDS IN SOME CASES
		if (button.contains("opt")) {
			char end = button.charAt(4);
			deselectAll(session);
			if (((int) session.getAttribute(button.charAt(0)+"play")) != 0) {
				if (end == '1') {
					session.setAttribute(button.charAt(0) + "opt2", (int) session.getAttribute(button.charAt(0)+"play"));
				} else {
					session.setAttribute(button.charAt(0) + "opt1", (int) session.getAttribute(button.charAt(0)+"play"));
				}
			}
			session.setAttribute(button.charAt(0)+"play", (int) session.getAttribute(button));
			session.setAttribute(button, 0);
		} else {
			if ((int) session.getAttribute(button.charAt(0)+"opt1") == 0) {
				session.setAttribute(button.charAt(0)+"opt1", (int)session.getAttribute(button));
				session.setAttribute(button, 0);
			} else {
				session.setAttribute(button.charAt(0)+"opt2", (int)session.getAttribute(button));
				session.setAttribute(button, 0);
			}
		}		
	}
	
	// Get game attributes
	public void getGameState(HttpSession session) {
		try {
			Connection connection = DBconnection.getConnectionToDatabase();			
			sql = "select * from onitama_games where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, (String) session.getAttribute("game_name"));
			set = statement.executeQuery();
			set.next();
			session.setAttribute("board_pos", set.getString("board_pos"));
			session.setAttribute("highlight", set.getString("highlight"));
			session.setAttribute("selectable", set.getString("selectable"));
			session.setAttribute("player_turn", set.getString("player_turn"));
			session.setAttribute("red", set.getString("red"));
			session.setAttribute("blue", set.getString("blue"));
			session.setAttribute("ropt1", set.getInt("ropt1"));
			session.setAttribute("ropt2", set.getInt("ropt2"));
			session.setAttribute("bopt1", set.getInt("bopt1"));
			session.setAttribute("bopt2", set.getInt("bopt2"));
			session.setAttribute("rnext", set.getInt("rnext"));
			session.setAttribute("bnext", set.getInt("bnext"));
			session.setAttribute("rplay", set.getInt("rplay"));
			session.setAttribute("bplay", set.getInt("bplay"));
			session.setAttribute("game_state", set.getInt("game_state"));
		} catch(SQLException exception) {
			exception.printStackTrace();
		}	
	}
	
	// Set game attributes
	public void setGameState(Connection connection, HttpSession session) {
		try {
			sql = "update onitama_games set board_pos=?, highlight=?, selectable=?, player_turn = ?, ropt1 = ?, ropt2 = ?, bopt1 = ?, bopt2 = ?, rnext = ?, bnext = ?, rplay = ?, bplay = ? where game_name = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, (String) session.getAttribute("board_pos"));
			statement.setString(2, (String) session.getAttribute("highlight"));
			statement.setString(3, (String) session.getAttribute("selectable"));
			statement.setString(4, (String) session.getAttribute("player_turn"));
			statement.setInt(5, (int) session.getAttribute("ropt1"));
			statement.setInt(6, (int) session.getAttribute("ropt2"));
			statement.setInt(7, (int) session.getAttribute("bopt1"));
			statement.setInt(8, (int) session.getAttribute("bopt2"));
			statement.setInt(9, (int) session.getAttribute("rnext"));
			statement.setInt(10, (int) session.getAttribute("bnext"));
			statement.setInt(11, (int) session.getAttribute("rplay"));
			statement.setInt(12, (int) session.getAttribute("bplay"));
			statement.setString(13, (String) session.getAttribute("game_name"));
			statement.executeUpdate();
		} catch(SQLException exception) {
			exception.printStackTrace();
		}	
	}
	
		
	// Create and populate the card deck for the game
	private void buildGameDeck(Connection connection, HttpSession session, String game_name) {
		ArrayList<Integer> deck = new ArrayList<>();
		for (int i = 0; i < 16; i++) {
			deck.add(i+1);
		}
		Collections.shuffle(deck);
		session.setAttribute("ropt1", deck.get(0));
		session.setAttribute("ropt2", deck.get(1));
		session.setAttribute("bopt1", deck.get(2));
		session.setAttribute("bopt2", deck.get(3));
		MoveCard card5 = new MoveCard(deck.get(4));
		if (card5.whichColor() == MoveCard.RED) { // Red goes first
			session.setAttribute("rnext", deck.get(4));
			session.setAttribute("bnext", 0);
			session.setAttribute("player_turn","r");
		} else { // Blue goes first
			session.setAttribute("rnext", 0);
			session.setAttribute("bnext", deck.get(4));
			session.setAttribute("player_turn","b");
		}
		setGameState(connection,session);
	}
	
	// Deselect all cells
	private void deselectAll(HttpSession session) {
		String selectable = "00000"
				+ "00000"
				+ "00000"
				+ "00000"
				+ "00000";	
		session.setAttribute("selectable", selectable);
	}	
	
	private void incrementGS(Connection connection,HttpSession session) {
		String game_name = (String) session.getAttribute("game_name");
		int game_state = ((int) session.getAttribute("game_state")) + 1;
		try {
			sql = "update onitama_games set game_state = ? where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, game_state);
			statement.setString(2, game_name);
			statement.executeUpdate();			
		} catch(SQLException exception) {
			exception.printStackTrace();
		}	
	}
	
}

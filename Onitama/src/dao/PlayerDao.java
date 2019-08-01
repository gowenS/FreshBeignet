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
				buildGameDeck(connection, session, gameNameAttempt);
			} else {
				return 2; // game doesn't exist error
			}			
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		return code;		
	}
	
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
		} catch(SQLException exception) {
			exception.printStackTrace();
		}	
	}
	
	
	
	// Create and populate the card deck for the game
	private void buildGameDeck(Connection connection, HttpSession session, String game_name) {
		ArrayList<Integer> deck = new ArrayList<>();
		for (int i = 0; i < 16; i++) {
			deck.add(i);
		}
		Collections.shuffle(deck);
		try {
			sql = "update onitama_games set ropt1 = ?, ropt2 = ?, bopt1 = ?, bopt2 = ?, rnext = ?, bnext = ?, player_turn = ? where game_name = ?";
			statement = connection.prepareStatement(sql);
			for (int j = 1; j < 5; j++) {
				statement.setInt(j, deck.get(j-1));
			}
			MoveCard card5 = new MoveCard(deck.get(4));
			if (card5.whichColor() == MoveCard.RED) { // Red goes first
				statement.setInt(5, deck.get(4));
				statement.setInt(6, 0);
				statement.setString(7, "r");
				session.setAttribute("player_turn","r");
			} else { // Blue goes first
				statement.setInt(5, 0);
				statement.setInt(6, deck.get(4));
				statement.setString(7, "b");
				session.setAttribute("player_turn","b");
			}
			statement.setString(8, game_name);
			System.out.println(statement.toString());
			statement.executeUpdate();
		} catch(SQLException exception) {
			exception.printStackTrace();
		}		
	}
}

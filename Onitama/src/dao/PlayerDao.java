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
				if(blue_exist != null) return 3; // lobby full error code
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
	
	// Main logic for player playing the game
	public boolean playerPlayingGame(HttpSession session) {
		boolean code = true;
		return code;
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
				statement.setNull(6, java.sql.Types.NULL);
				statement.setString(7, "r");
			} else { // Blue goes first
				statement.setNull(5, java.sql.Types.NULL);
				statement.setInt(6, deck.get(4));
				statement.setString(7, "b");
			}
			statement.setString(8, game_name);
			System.out.println(statement.toString());
;			statement.executeUpdate();
		} catch(SQLException exception) {
			exception.printStackTrace();
		}		
	}
}

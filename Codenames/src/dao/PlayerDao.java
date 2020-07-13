package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import classes.GameBoard;
import classes.WordList;
import servlets.RefreshServlet;

public class PlayerDao {
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	public void buttonPress(HttpSession session, String btn) {
		getGameState(session);
		System.out.println("Button pressed was " + btn);
		switch(btn) {
			case "v":
				String myColor = myFullColor((String)session.getAttribute("player_color"));
				session.setAttribute("spy_"+ myColor, (int)session.getAttribute("my_ID"));
				String oppColor = myOppColor(myColor);
				if( !(((int) session.getAttribute("spy_" + oppColor)) == 0) ) {
					startPlaying(session);
				}
				break;
			case "submit_selected":
				checkCorrectGuess(session);
				break;
			case "end_turn":
				endTurn(session);
				break;
			case "change_teams":
				session.setAttribute("round_num", 0);
				session.setAttribute("spy_red", 0);
				session.setAttribute("spy_blue", 0);
				break;
			case "play_again":
				session.setAttribute("round_num", (int)session.getAttribute("round_num") + 1);
				session.setAttribute("game_phase", "v");
				session.setAttribute("spy_red", 0);
				session.setAttribute("spy_blue", 0);
				break;
			default: // Catch all should be numeric identifier for button pressed.
				buttonWasSelected(session,btn);
		}
		setGameState(session);
	}
	
	private void startPlaying(HttpSession session) {
		String gameName = (String)session.getAttribute("gameName");
		GameBoard gb = new GameBoard();
		session.setAttribute("board_colors", gb.getGameBoard());
		session.setAttribute("turn", gb.getBoardColor());
		//set words
		session.setAttribute("revealed", "0000000000000000000000000");
		session.setAttribute("selected", "0000000000000000000000000");
		session.setAttribute("game_phase", "p");
		session.setAttribute("clue", "");
		session.setAttribute("clue_number", 0);
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			sql = "select * from codenames_wordbank where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameName);
			set = statement.executeQuery();
			set.next();
			WordList wl = new WordList(set.getString("words_remaining"));
			String[] wordResults = wl.getNewGameWordList();
			session.setAttribute("words", wordResults[0]);
			sql = "update codenames_wordbank set words_remaining = ? where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, wordResults[1]);
			statement.setString(2, gameName);
			statement.executeUpdate();
			
		}catch(SQLException exception) {
			exception.printStackTrace();
		}	
	}
	
	public void getGameState(HttpSession session) {
		String gameName = (String) session.getAttribute("gameName");
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			sql = "select a.*, b.player_name as spy_red_name, c.player_name as spy_blue_name from codenames_games a"
					+ " left outer join " + gameName + "_players b on a.spy_red = b.player_id"
					+ " left outer join " + gameName + "_players c on a.spy_blue = c.player_id where a.game_name = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameName);
			set = statement.executeQuery();
			set.next();
			session.setAttribute("turn", set.getString("turn"));
			session.setAttribute("words", set.getString("words"));
			session.setAttribute("board_colors", set.getString("board_colors"));
			session.setAttribute("revealed", set.getString("revealed"));
			session.setAttribute("selected", set.getString("selected"));
			session.setAttribute("spy_red", set.getInt("spy_red"));
			session.setAttribute("spy_blue", set.getInt("spy_blue"));
			session.setAttribute("clue", set.getString("clue"));
			session.setAttribute("clue_number", set.getInt("clue_number"));
			session.setAttribute("round_num", set.getInt("round_num"));
			session.setAttribute("game_phase", set.getString("game_phase"));
			session.setAttribute("spy_red_name", set.getString("spy_red_name"));
			session.setAttribute("spy_blue_name", set.getString("spy_blue_name"));	
			session.setAttribute("gameState", RefreshServlet.getGameState(gameName));	
//			System.out.println("gameState " + RefreshServlet.getGameState(gameName));
		} catch(SQLException exception) {
			exception.printStackTrace();
		}		
	}
	
	private void setGameState(HttpSession session) {
		String gameName = (String) session.getAttribute("gameName");
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			sql = "update codenames_games set turn = ?, words = ?, board_colors = ?, revealed = ?, spy_red = ?, spy_blue = ?, clue = ?, clue_number = ?, round_num = ?, game_phase = ?, selected = ? where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, (String)session.getAttribute("turn"));
			statement.setString(2, (String)session.getAttribute("words"));
			statement.setString(3, (String)session.getAttribute("board_colors"));
			statement.setString(4, (String)session.getAttribute("revealed"));
			statement.setInt(5, (int)session.getAttribute("spy_red"));
			statement.setInt(6, (int)session.getAttribute("spy_blue"));
			statement.setString(7, (String)session.getAttribute("clue"));
			statement.setInt(8, (int)session.getAttribute("clue_number"));
			statement.setInt(9, (int)session.getAttribute("round_num"));
			statement.setString(10, (String)session.getAttribute("game_phase"));
			statement.setString(11, (String)session.getAttribute("selected"));
			statement.setString(12, gameName);
			statement.executeUpdate();
			incrementGS(session);
		}catch(SQLException exception) {
			exception.printStackTrace();
		}	
	}
	
	public void enterClue(HttpSession session, String clue, String clue_number_str) {
		System.out.println("Entering clue?");
		int clue_number = Integer.parseInt(clue_number_str);
		session.setAttribute("clue", clue);
		session.setAttribute("clue_number", clue_number);
		setGameState(session);
	}
	
	private void buttonWasSelected(HttpSession session, String btn) {
		StringBuilder newSelected = new StringBuilder("0000000000000000000000000");
		newSelected.setCharAt(Integer.parseInt(btn), '1');
		session.setAttribute("selected", newSelected.toString());
	}
	
	private void checkCorrectGuess(HttpSession session) {
		String player_color = (String) session.getAttribute("player_color");
		String board_colors = (String) session.getAttribute("board_colors");
		String selected = (String) session.getAttribute("selected");
		int num = selected.indexOf("1");
		StringBuilder revealed = new StringBuilder((String)session.getAttribute("revealed"));
		revealed.setCharAt(num, '1');
		if (board_colors.charAt(num) == 'a') {
			gameOver("l", session);
		} else if (board_colors.charAt(num) == 'n') {
			endTurn(session);			
		} else if (board_colors.charAt(num) == player_color.charAt(0)) {
			// check if win has been achieved
			if (checkForWin(player_color,board_colors,revealed.toString())) {
				gameOver("w", session);
			} else {
				// de-increment remaining guesses
			}			
		} else {
			// Guess other team's word
			if (checkForWin(myOppColor(player_color),board_colors,revealed.toString())) {
				gameOver("l", session);
			} else {
				endTurn(session);
			}						
		}		
		session.setAttribute("revealed", revealed.toString());
	}
	
	private Boolean checkForWin(String player_color, String board_colors, String revealed) {
		Boolean out = true;
		for(int i = 0; i < board_colors.length(); i++) {
			if (board_colors.charAt(i) == player_color.charAt(0) && revealed.charAt(i) != '1') out = false;
		}
		return out;
	}
	
	private void gameOver(String win_loss, HttpSession session) {
		session.setAttribute("game_phase", win_loss);
	}
	
	private void endTurn(HttpSession session) {
		session.setAttribute("selected", "0000000000000000000000000");
		session.setAttribute("clue", "");
		session.setAttribute("clue_number", 0);
		session.setAttribute("turn", ((String)session.getAttribute("turn")).equals("r") ? "b" : "r");
		System.out.println("turn is now " + ((String)session.getAttribute("turn")));
	}
	
	private String myFullColor(String color) {
		String out = "blue";
		if (color.charAt(0) == 'r') {
			out = "red";
		}
		return out;
	}
	
	private String myOppColor(String color) {
		String out = "red";
		if (color.charAt(0) == 'r') {
			out = "blue";
		}
		return out;
	}
	
	private void incrementGS(HttpSession session) {
		String gameName = (String) session.getAttribute("gameName");	
		RefreshServlet.incrementGameState(gameName);
	}		
}

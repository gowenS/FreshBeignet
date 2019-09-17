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
import servlets.RefreshServlet;

public class PlayerDao {
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	public void buttonPress(HttpSession session, String btn) {
		getGameState(session);
		if (btn.contentEquals("v")) {
			String myColor = myFullColor((String)session.getAttribute("player_color"));
			session.setAttribute("spy_"+myColor, myColor.charAt(0));
			String oppColor = myOppColor(myColor);
			if(!((String) session.getAttribute("spy_" + oppColor)).equals("")) {
				startPlaying(session);
			}
		}
		setGameState(session);
	}
	
	private void startPlaying(HttpSession session) {
		GameBoard gb = new GameBoard();
		session.setAttribute("board_values", gb.getGameBoard());
		session.setAttribute("turn", gb.getBoardColor().charAt(0));
		//set words
		session.setAttribute("revealed", "0000000000000000000000000");
	}
	
	public void getGameState(HttpSession session) {
		String gameName = (String) session.getAttribute("gameName");
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			sql = "select * from codenames_games where game_name = ?";
			statement = connection.prepareStatement(sql);
			set = statement.executeQuery();
			set.next();
			session.setAttribute("turn", set.getString("turn"));
			session.setAttribute("words", set.getString("words"));
			session.setAttribute("board_values", set.getString("board_values"));
			session.setAttribute("revealed", set.getString("revealed"));
			session.setAttribute("spy_red", set.getInt("spy_red"));
			session.setAttribute("spy_blue", set.getInt("spy_blue"));
			session.setAttribute("clue", set.getString("clue"));
			session.setAttribute("clue_number", set.getInt("clue_number"));
			session.setAttribute("round_num", set.getInt("round_num"));
			session.setAttribute("gamePhase", set.getString("game_phase"));
			session.setAttribute("gameState", RefreshServlet.getGameState(gameName));
		} catch(SQLException exception) {
			exception.printStackTrace();
		}		
	}
	
	private void setGameState(HttpSession session) {
		String gameName = (String) session.getAttribute("gameName");
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			sql = "update codenames_games set turn = ?, words = ?, board_values = ?, revealed = ?, spy_red = ?, spy_ blue = ?, clue = ?, clue_number = ?, round_num = ?, game_phase = ? where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, (String)session.getAttribute("turn"));
			statement.setString(2, (String)session.getAttribute("words"));
			statement.setString(3, (String)session.getAttribute("board_values"));
			statement.setString(4, (String)session.getAttribute("revealed"));
			statement.setInt(5, (int)session.getAttribute("spy_red"));
			statement.setInt(6, (int)session.getAttribute("spy_blue"));
			statement.setString(7, (String)session.getAttribute("clue"));
			statement.setInt(8, (int)session.getAttribute("clue_number"));
			statement.setInt(9, (int)session.getAttribute("round_num"));
			statement.setString(10, (String)session.getAttribute("game_phase"));
			statement.setString(11, gameName);
			incrementGS(session);
		}catch(SQLException exception) {
			exception.printStackTrace();
		}	
	}
	
	private String myFullColor(String color) {
		String out = "blue";
		if (color.equals("r")) {
			out = "red";
		}
		return out;
	}
	
	private String myOppColor(String color) {
		String out = "red";
		if (color.equals("r")) {
			out = "blue";
		}
		return out;
	}
	
	private void incrementGS(HttpSession session) {
		String gameName = (String) session.getAttribute("game_name");	
		RefreshServlet.incrementGameState(gameName);
	}		
}

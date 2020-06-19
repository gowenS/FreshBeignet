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
			session.setAttribute("spy_"+ myColor, (int)session.getAttribute("my_ID"));
			String oppColor = myOppColor(myColor);
			if( ! (((int) session.getAttribute("spy_" + oppColor)) == 0) ) {
				startPlaying(session);
			}
		}
		setGameState(session);
	}
	
	private void startPlaying(HttpSession session) {
		GameBoard gb = new GameBoard();
		session.setAttribute("board_colors", gb.getGameBoard());
		session.setAttribute("turn", gb.getBoardColor().charAt(0));
		//set words
		session.setAttribute("revealed", "0000000000000000000000000");
		session.setAttribute("game_phase", "p");
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
			session.setAttribute("spy_red", set.getInt("spy_red"));
			session.setAttribute("spy_blue", set.getInt("spy_blue"));
			session.setAttribute("clue", set.getString("clue"));
			session.setAttribute("clue_number", set.getInt("clue_number"));
			session.setAttribute("round_num", set.getInt("round_num"));
			session.setAttribute("game_phase", set.getString("game_phase"));
			session.setAttribute("spy_red_name", set.getString("spy_red_name"));
			session.setAttribute("spy_blue_name", set.getString("spy_blue_name"));	
			session.setAttribute("gameState", RefreshServlet.getGameState(gameName));		
		} catch(SQLException exception) {
			exception.printStackTrace();
		}		
	}
	
	private void setGameState(HttpSession session) {
		String gameName = (String) session.getAttribute("gameName");
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			sql = "update codenames_games set turn = ?, words = ?, board_colors = ?, revealed = ?, spy_red = ?, spy_blue = ?, clue = ?, clue_number = ?, round_num = ?, game_phase = ? where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, session.getAttribute("turn").toString());
			statement.setString(2, (String)session.getAttribute("words"));
			statement.setString(3, (String)session.getAttribute("board_colors"));
			statement.setString(4, (String)session.getAttribute("revealed"));
			statement.setInt(5, (int)session.getAttribute("spy_red"));
			statement.setInt(6, (int)session.getAttribute("spy_blue"));
			statement.setString(7, (String)session.getAttribute("clue"));
			statement.setInt(8, (int)session.getAttribute("clue_number"));
			statement.setInt(9, (int)session.getAttribute("round_num"));
			statement.setString(10, (String)session.getAttribute("game_phase"));
			statement.setString(11, gameName);
			statement.executeUpdate();
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
		String gameName = (String) session.getAttribute("gameName");	
		RefreshServlet.incrementGameState(gameName);
	}		
}

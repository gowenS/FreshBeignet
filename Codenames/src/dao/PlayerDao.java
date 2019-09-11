package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import servlets.RefreshServlet;

public class PlayerDao {
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	public void getGameState(HttpSession session) {
		String gameName = (String) session.getAttribute("gameName");
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			sql = "select * from codenames_games where game_name = ?";
			statement = connection.prepareStatement(sql);
			
			session.setAttribute("gameState", RefreshServlet.getGameState(gameName));
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		
		
	}
	
	private void incrementGS(Connection connection,HttpSession session) {
		String gameName = (String) session.getAttribute("game_name");	
		RefreshServlet.incrementGameState(gameName);
	}		
}

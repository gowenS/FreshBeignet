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
	
	
	private void incrementGS(Connection connection,HttpSession session) {
		String game_name = (String) session.getAttribute("game_name");	
		RefreshServlet.incrementGameState(game_name);
	}		
}

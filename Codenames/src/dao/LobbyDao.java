package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import servlets.RefreshServlet;

public class LobbyDao {
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	public int getRound(HttpSession session) {
		String gameName = (String) session.getAttribute("gameName");
		int out = 0;
		try {
			Connection connection = DBconnection.getConnectionToDatabase();			
			sql = "select round_num from codenames_games where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameName);
			set = statement.executeQuery();
			set.next();
			out = set.getInt("round_num");
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		return out;
	}
	
	public void getTeams(HttpSession session) {
		String gameName = (String) session.getAttribute("gameName");
		StringBuilder redTeam = new StringBuilder();
		StringBuilder blueTeam = new StringBuilder();
		StringBuilder noTeam = new StringBuilder();
		String curName = "";
		String curTeam = "";
		try {
			Connection connection = DBconnection.getConnectionToDatabase();	
			sql = "select * from " + gameName + "_players";
			statement = connection.prepareStatement(sql);
			set = statement.executeQuery();
			while(set.next()) {
				curName = set.getString("player_name");
				curTeam = set.getString("player_color");
				if (curTeam.equals("r")) {
					redTeam.append(curName);
					redTeam.append(',');
				} else if (curTeam.equals("b")) {
					blueTeam.append(curName);
					blueTeam.append(',');
				} else {
					noTeam.append(curName);
					noTeam.append(',');
				}
			}
			if (redTeam.length() > 1) redTeam.deleteCharAt(redTeam.length()-1);
			if (blueTeam.length() > 1) blueTeam.deleteCharAt(blueTeam.length()-1);
			if (noTeam.length() > 1) noTeam.deleteCharAt(noTeam.length()-1);
			session.setAttribute("redTeam", redTeam.toString());
			session.setAttribute("blueTeam", blueTeam.toString());
			session.setAttribute("noTeam", noTeam.toString());
		} catch(SQLException exception) {
			exception.printStackTrace();
		}		
	}
	
	public void buttonPress(String button,HttpSession session) {
		String gameName = (String) session.getAttribute("gameName");
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			if (button.contentEquals("p")) {
				sql = "update codenames_games set round_num = 1 where game_name like ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, gameName);
				statement.executeUpdate();
			} else {
				sql = "update " + gameName + "_players set player_color = ? where player_name like ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, button);
				statement.setString(2, (String)session.getAttribute("playerName"));
				statement.executeUpdate();
				session.setAttribute("player_color", button);
			}			
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		RefreshServlet.incrementGameState(gameName);
	}
	
}

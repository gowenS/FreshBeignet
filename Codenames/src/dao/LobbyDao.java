package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
}

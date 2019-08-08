package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.http.HttpSession;

public class SharedDao {	
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	public Boolean checkForRefresh(HttpSession session) {
		Connection connection = DBconnection.getConnectionToDatabase();
		String game_name = (String) session.getAttribute("game_name");
		int gs1 = (int) session.getAttribute("game_state");
		int gs2 = gs1;
		sql = "select game_state from onitama_games where game_name = ?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, game_name);
			set = statement.executeQuery();
			set.next();
			gs2 = set.getInt("game_state");
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		if (gs2 > gs1) {
			return true;
		}
		return false;		
	}

}

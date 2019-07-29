package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class PlayerDao {
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	// Player joining game 
	public int joinGame(String gameNameAttempt, String playerNameAttempt){
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
				sql = "insert into " + gameNameAttempt + "(blue) values (?)";
				statement = connection.prepareStatement(sql);
				statement.setString(1, playerNameAttempt);
				statement.executeUpdate();
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
	

}

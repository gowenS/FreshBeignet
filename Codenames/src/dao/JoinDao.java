package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import servlets.RefreshServlet;

public class JoinDao {
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	// Player joining game 
	public int joinGame(String gameNameAttempt, String playerNameAttempt, HttpSession session){
		int code = 0;
		try {			
			Connection connection = DBconnection.getConnectionToDatabase();			
			sql = "select * from codenames_games where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameNameAttempt);
			set = statement.executeQuery();
			if (set.next()) {
				code = 1; // success
				// TODO make a lobby full check return 3; // lobby full error code
				sql = "select player_name from " + gameNameAttempt + "_players where player_name like ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, playerNameAttempt);
				set = statement.executeQuery();
				if(set.next()) return 4; // player name already taken error code
				sql = "insert into " + gameNameAttempt + "_players (player_name, player_color) values (?, ?)";
				statement = connection.prepareStatement(sql);
				statement.setString(1, playerNameAttempt);
				statement.setString(2, "n");
				statement.executeUpdate();
				session.setAttribute("gameName", gameNameAttempt);
				session.setAttribute("playerColor", "n");
				session.setAttribute("playerName", playerNameAttempt);
				
				sql = "select * from " + gameNameAttempt + "_players where player_name = ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, playerNameAttempt);
				set = statement.executeQuery();
				set.next();
				session.setAttribute("my_ID", set.getInt("player_id"));
				
				RefreshServlet.incrementGameState(gameNameAttempt);
			} else {
				return 2; // game doesn't exist error
			}			
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		return code;		
	}	
	
}

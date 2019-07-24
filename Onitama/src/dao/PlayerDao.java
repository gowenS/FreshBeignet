package dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class PlayerDao {
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	// Player joining game 
	public int joinGame(String gameNameAttempt, String playerNameAttempt, HttpSession session){
		int code = 0;
		try {			
			Connection connection = DBconnection.getConnectionToDatabase();			
			sql = "select game_name from games where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameNameAttempt);
			set = statement.executeQuery();
			if (set.next()) {
				code = 1; // success
				sql = "select tot_players, players_in_lobby from onitama_games where game_name like ?";				
				statement = connection.prepareStatement(sql);
				statement.setString(1, gameNameAttempt);
				set = statement.executeQuery();
				set.next();
				int playersLoaded = set.getInt("players_in_lobby");
				int numPlayers = set.getInt("tot_players");
				if(playersLoaded >= numPlayers) return 3; // lobby full error code
				sql = "select player_name from " + gameNameAttempt + " where player_name like ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, playerNameAttempt);
				set = statement.executeQuery();
				if(set.next()) return 4; // player name already taken error code
				sql = "update games set players_in_lobby = " + Integer.toString(playersLoaded+1) + " where game_name like ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, gameNameAttempt);
				statement.executeUpdate();
				sql = "insert into " + gameNameAttempt + "(player_id, player_name) values ("+ Integer.toString(playersLoaded+1) 
				+", ?)";
				statement = connection.prepareStatement(sql);
				statement.setString(1, playerNameAttempt);
				statement.executeUpdate();
				session.setAttribute("myState", 1);
				session.setAttribute("player_id", (playersLoaded+1));
				session.setAttribute("gameName", gameNameAttempt);
				session.setAttribute("myName", playerNameAttempt);
				session.setAttribute("numPlayers", numPlayers);
				session.setAttribute("refresh_now", true);
			} else {
				session.setAttribute("gameState", 0);
				session.setAttribute("player_id", null);
				session.setAttribute("gameName", null);
				session.setAttribute("myName", null);
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

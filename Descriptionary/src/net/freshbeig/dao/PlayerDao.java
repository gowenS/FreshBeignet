package net.freshbeig.dao;

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
				sql = "select tot_players, players_in_lobby from games where game_name like ?";				
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
		int myState = (int)session.getAttribute("myState");
		String gameName = (String)session.getAttribute("gameName");
		int myID = (int) session.getAttribute("player_id");
		boolean refreshNow = true; // keep checking
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			sql = "select game_state from games where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, gameName);
			set = statement.executeQuery();
			set.next();
			int gameState = set.getInt("game_state");
			session.setAttribute("gameState", gameState);
			int playerCount = (int) session.getAttribute("numPlayers");
			if(myState == gameState && gameState <= playerCount) {
				refreshNow = false;  // need to update game state
				if(gameState>1) {
					String turnReturn = "turn" + (gameState-1);
					sql = "select " + turnReturn + " from " + gameName + " where player_id = ?";
					statement = connection.prepareStatement(sql);
					int pullFrom;
					if(myID == 1) {
						pullFrom = playerCount;
					} else {
						pullFrom = myID - 1;
					}
					statement.setInt(1, pullFrom);
					set = statement.executeQuery();
					set.next();			
					if(gameState%2 != 0) {
						//Drawing phase									
						session.setAttribute("text_description", set.getString(turnReturn));
					} else {
						//Writing phase							
						session.setAttribute("img_path", set.getString(turnReturn));						
					}
				}
			}
			connection.close();			
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		return refreshNow;
	}
	
	// Writes an image to the server and saves image path in game instance table in MySQL
	public void placeDwgTurn(HttpSession session, HttpServletRequest req) {
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			String gameName = (String) session.getAttribute("gameName");
			int myState =(int) session.getAttribute("myState");
			String dwg = Integer.toString(myState);
			int myID = (int) session.getAttribute("player_id");
			String image_encoded = req.getParameter("img_from_player").substring(22);
			byte[] image_byte = Base64.getDecoder().decode(image_encoded);
			sql = "select * from imagesfilepath";
			statement = connection.prepareStatement(sql);
			set = statement.executeQuery();
			set.next();
			String system_path = set.getString("path");			
			String newImgName = gameName + "_" + myState + "_" + myID + ".png";
			String filepath = system_path + newImgName;
			try {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(image_byte));
				if (image == null) {
					System.out.println("Buffered Image is null");
				}
				File f = new File(filepath);
				ImageIO.write(image, "png", f);
			} catch(IOException exception) {
				exception.printStackTrace();
			}
			sql = "update " + gameName + " set turn" + myState + " = ? where player_id = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, newImgName);
			statement.setInt(2, myID);
			statement.executeUpdate();
		} catch(SQLException exception){
			exception.printStackTrace();
		}
	}

	// Places a player's description of a drawing into the game table in MySQL database
	public void placeDescTurn(HttpSession session) {
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			String txtDesc = (String) session.getAttribute("text_description");
			String gameName = (String) session.getAttribute("gameName");
			int myState =(int) session.getAttribute("myState");
			int myID = (int) session.getAttribute("player_id");
			sql = "update " + gameName +" set turn"+ myState+ " = ? where player_id = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, txtDesc);
			statement.setInt(2, myID);
			statement.executeUpdate();
		} catch(SQLException exception){
			exception.printStackTrace();
		}
	}

}

package net.freshbeig.dao;

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
	
	// Get the "Stack" of history of a single player's drawing, including all corresponding descriptions
	// and drawings from other players
	public LinkedList<String> getStack(HttpSession session, String gameName, int player, int numPlayers) {
		LinkedList<String> stackOut = new LinkedList<String>();
		try {
			Connection connection = DBconnection.getConnectionToDatabase();
			String turnID;			
			for(int i = 1; i <= numPlayers; i++) {
				turnID = "turn" + i;
				sql = "select player_name," + turnID + " from " + gameName + " where player_id = ?";
				statement = connection.prepareStatement(sql);
				int pID = player + (i-1);
				if(pID > numPlayers) pID = pID - numPlayers;
				statement.setInt(1, pID);
				set = statement.executeQuery();
				set.next();
				stackOut.add(set.getString("player_name"));
				stackOut.add(set.getString(turnID));				
			}			
			sql = "select player_name from " + gameName + " where player_id = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1,player);
			set = statement.executeQuery();
			set.next();
			session.setAttribute("current_name", set.getString("player_name"));			
		} catch(SQLException exception) {
			exception.printStackTrace();
		}		
		return stackOut;
	}

}

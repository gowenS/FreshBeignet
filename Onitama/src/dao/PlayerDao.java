package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import classes.MoveCard;
import servlets.RefreshServlet;

public class PlayerDao {
	PreparedStatement statement;
	String sql;
	ResultSet set;
	
	// Player joining game 
	public int joinGame(String gameNameAttempt, String playerNameAttempt, HttpSession session){
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
				if(blue_exist.length() > 0) return 3; // lobby full error code
				sql = "update onitama_games set blue = ? where game_name = ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, playerNameAttempt);
				statement.setString(2, gameNameAttempt);
				statement.executeUpdate();
				session.setAttribute("game_name", gameNameAttempt);
				session.setAttribute("player_color", "blue");
				getGameState(session);
				buildGameDeck(connection, session, gameNameAttempt);
				incrementGS(connection, session);
			} else {
				return 2; // game doesn't exist error
			}			
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		return code;		
	}
	
	// Handles button press
	public void buttonPress(String button,HttpSession session) {
		if(((int)session.getAttribute("win")) != 1 ) {
			Connection connection = DBconnection.getConnectionToDatabase();
			getGameState(session);
			if (button.contains("opt") || button.contains("play")) {
				chooseMove(button,session,connection);
			} else {
				gridPress(button,session);
			}
			incrementGS(connection,session);
			setGameState(connection,session);
		} else {
			if (button.equals("new_game")) {
				resetGame(session);
			}
		}
	}
	
	// Grid cell is pressed
	private void gridPress(String button, HttpSession session) {
		String selectable = (String) session.getAttribute("selectable");
		StringBuilder board_pos = new StringBuilder((String) session.getAttribute("board_pos"));
		if (((int)session.getAttribute(((String)session.getAttribute("player_color")).charAt(0)+"play"))!=0){ // Is a move card chosen?
			if (amIme(button,session)) {
				deselectAll(session);
				showLegalMoves(button,session);
			}
			if (selectable.charAt(Integer.parseInt(button)) == '1') {
				if (opposingKing(button,session)) {
					session.setAttribute("win", 1);
				}
				if (Integer.parseInt(button) == 2 && (board_pos.charAt((int) session.getAttribute("move_player")) == 'l' || board_pos.charAt((int) session.getAttribute("move_player")) == 'j')) {
					session.setAttribute("win", 1);
				}
				board_pos.setCharAt(Integer.parseInt(button), board_pos.charAt((int) session.getAttribute("move_player")));
				if ((int) session.getAttribute("move_player") == 2) {
					if (((String) session.getAttribute("player_color")).charAt(0) == 'r') {
						board_pos.setCharAt((int)session.getAttribute("move_player"), 'w');
					} else {
						board_pos.setCharAt((int)session.getAttribute("move_player"), 'q');
					}				
				} else if (((int) session.getAttribute("move_player")) == 22){
					if (((String) session.getAttribute("player_color")).charAt(0) == 'b') {
						board_pos.setCharAt((int)session.getAttribute("move_player"), 'w');
					} else {
						board_pos.setCharAt((int)session.getAttribute("move_player"), 'q');
					}	
				} else {
					board_pos.setCharAt((int)session.getAttribute("move_player"), 'n');
				}		
				deselectAll(session);
				session.setAttribute("move_player", null);
				distributeCards(session);
				if ((int) session.getAttribute("win") == 1) {
					//gameOver(session);
				}
			}	
		}
		session.setAttribute("board_pos", board_pos.toString());
	}
	
	
	// Shows the available moves for the player selecting the player piece
	private void showLegalMoves(String button, HttpSession session) {
		session.setAttribute("move_player", Integer.parseInt(button));
		String player_color = (String) session.getAttribute("player_color");
        String selectable; 
		MoveCard cardChosen = new MoveCard((int) session.getAttribute(player_color.charAt(0) + "play"));
		int[] movePlayer = convNumToPos(button);
		int row = movePlayer[0];
		int col = movePlayer[1];
		int rowCheck = -1;
		int colCheck = -1;
		int[] moveRow = cardChosen.getRow();
		int[] moveCol = cardChosen.getCol();
		for (int spot = 0; spot < 4; spot++) {
			rowCheck = row - moveRow[spot];
            colCheck = col + moveCol[spot];
			if ((rowCheck > -1 && rowCheck < 5) && (colCheck > -1 && colCheck < 5)) {
                int checkHere = rowCheck*5 + colCheck;
                if (!amIme(Integer.toString(checkHere),session)) {
                	selectable = (String) session.getAttribute("selectable");
                	StringBuilder new_selectable = new StringBuilder(selectable);
                    new_selectable.setCharAt(checkHere, '1');
                    session.setAttribute("selectable", new_selectable.toString());
                    System.out.println(new_selectable.toString());
                }
            }			
		}
	}
	
	// Convert single num pos to [row,col] notation	
	private int[] convNumToPos(String in) {
		int num = Integer.parseInt(in);
		int[] out = new int[2];
		out[0] = num/5;
		out[1] = num%5;
 		return out;
	}	
	
	// Determine if player piece selected is opposing king
		private Boolean opposingKing(String button, HttpSession session) {
			Boolean out = false;
			String player_color = (String) session.getAttribute("player_color");
			String board_pos = (String) session.getAttribute("board_pos");
			if (player_color.charAt(0) == 'r') {
				if (board_pos.charAt(Integer.parseInt(button)) == 'l') {
					return true;
				}
			} else {
				if (board_pos.charAt(Integer.parseInt(button)) == 'j') {
					return true;
				}		
			}
			return out;
		}
	
	// Determine if player piece selected belongs to the player who selected the piece
	private Boolean amIme(String button, HttpSession session) {
		Boolean out = false;
		String player_color = (String) session.getAttribute("player_color");
		String board_pos = (String) session.getAttribute("board_pos");
		if (player_color.charAt(0) == 'r') {
			if ((board_pos.charAt(Integer.parseInt(button)) == 'r') || (board_pos.charAt(Integer.parseInt(button)) == 'j')) {
				return true;
			}
		} else {
			if ((board_pos.charAt(Integer.parseInt(button)) == 'b') || (board_pos.charAt(Integer.parseInt(button)) == 'l')) {
				return true;
			}		
		}
		return out;
	}
	
	// Move Card is pressed
	private void chooseMove(String button,HttpSession session,Connection connection) {
		session.setAttribute("move_player", null);
		if (button.contains("opt")) {
			char end = button.charAt(4);
			deselectAll(session);
			if (((int) session.getAttribute(button.charAt(0)+"play")) != 0) {
				if (end == '1') {
					session.setAttribute(button.charAt(0) + "opt2", (int) session.getAttribute(button.charAt(0)+"play"));
				} else {
					session.setAttribute(button.charAt(0) + "opt1", (int) session.getAttribute(button.charAt(0)+"play"));
				}
			}
			session.setAttribute(button.charAt(0)+"play", (int) session.getAttribute(button));
			session.setAttribute(button, 0);
		} else {
			deselectAll(session);
			if ((int) session.getAttribute(button.charAt(0)+"opt1") == 0) {
				session.setAttribute(button.charAt(0)+"opt1", (int)session.getAttribute(button));
				session.setAttribute(button, 0);
			} else {
				session.setAttribute(button.charAt(0)+"opt2", (int)session.getAttribute(button));
				session.setAttribute(button, 0);
			}
		}		
	}
	
	// Get game attributes
	public void getGameState(HttpSession session) {
		String game_name = (String) session.getAttribute("game_name");
		String player_color = (String) session.getAttribute("player_color");
		try {
			Connection connection = DBconnection.getConnectionToDatabase();			
			sql = "select * from onitama_games where game_name like ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, game_name);
			set = statement.executeQuery();
			set.next();
			if (player_color.charAt(0) == 'r') {
				session.setAttribute("board_pos", set.getString("board_pos"));
				session.setAttribute("highlight", set.getString("highlight"));
				session.setAttribute("selectable", set.getString("selectable"));
			} else {
				session.setAttribute("board_pos", reverseString(set.getString("board_pos")));
				session.setAttribute("highlight", reverseString(set.getString("highlight")));
				session.setAttribute("selectable", reverseString(set.getString("selectable")));
			}			
			session.setAttribute("player_turn", set.getString("player_turn"));
			session.setAttribute("red", set.getString("red"));
			session.setAttribute("blue", set.getString("blue"));
			session.setAttribute("ropt1", set.getInt("ropt1"));
			session.setAttribute("ropt2", set.getInt("ropt2"));
			session.setAttribute("bopt1", set.getInt("bopt1"));
			session.setAttribute("bopt2", set.getInt("bopt2"));
			session.setAttribute("rnext", set.getInt("rnext"));
			session.setAttribute("bnext", set.getInt("bnext"));
			session.setAttribute("rplay", set.getInt("rplay"));
			session.setAttribute("bplay", set.getInt("bplay"));
			session.setAttribute("game_state", RefreshServlet.getGameState(game_name));
			session.setAttribute("win", set.getInt("win"));
		} catch(SQLException exception) {
			exception.printStackTrace();
		}	
	}
	
	// Set game attributes
	public void setGameState(Connection connection, HttpSession session) {
		String player_color = (String) session.getAttribute("player_color");
		try {
			sql = "update onitama_games set board_pos=?, highlight=?, selectable=?, player_turn = ?, ropt1 = ?, ropt2 = ?, bopt1 = ?, bopt2 = ?, rnext = ?, bnext = ?, rplay = ?, bplay = ?, win = ? where game_name = ?";
			statement = connection.prepareStatement(sql);
			if (player_color.charAt(0) == 'r') {
				statement.setString(1, (String) session.getAttribute("board_pos"));
				statement.setString(2, (String) session.getAttribute("highlight"));
				statement.setString(3, (String) session.getAttribute("selectable"));
			} else {				
				statement.setString(1, reverseString((String) session.getAttribute("board_pos")));
				statement.setString(2, reverseString((String) session.getAttribute("highlight")));
				statement.setString(3, reverseString((String) session.getAttribute("selectable")));
			}			
			statement.setString(4, (String) session.getAttribute("player_turn"));
			statement.setInt(5, (int) session.getAttribute("ropt1"));
			statement.setInt(6, (int) session.getAttribute("ropt2"));
			statement.setInt(7, (int) session.getAttribute("bopt1"));
			statement.setInt(8, (int) session.getAttribute("bopt2"));
			statement.setInt(9, (int) session.getAttribute("rnext"));
			statement.setInt(10, (int) session.getAttribute("bnext"));
			statement.setInt(11, (int) session.getAttribute("rplay"));
			statement.setInt(12, (int) session.getAttribute("bplay"));
			statement.setInt(13, (int) session.getAttribute("win"));
			statement.setString(14, (String) session.getAttribute("game_name"));
			statement.executeUpdate();
		} catch(SQLException exception) {
			exception.printStackTrace();
		}	
	}	
		
	// Create and populate the card deck for the game
	private void buildGameDeck(Connection connection, HttpSession session, String game_name) {
		ArrayList<Integer> deck = new ArrayList<>();
		for (int i = 0; i < 16; i++) {
			deck.add(i+1);
		}
		Collections.shuffle(deck);
		session.setAttribute("ropt1", deck.get(0));
		session.setAttribute("ropt2", deck.get(1));
		session.setAttribute("bopt1", deck.get(2));
		session.setAttribute("bopt2", deck.get(3));
		MoveCard card5 = new MoveCard(deck.get(4));
		if (card5.whichColor() == MoveCard.RED) { // Red goes first
			session.setAttribute("rnext", deck.get(4));
			session.setAttribute("bnext", 0);
			session.setAttribute("player_turn","r");
		} else { // Blue goes first
			session.setAttribute("rnext", 0);
			session.setAttribute("bnext", deck.get(4));
			session.setAttribute("player_turn","b");
		}
		setGameState(connection,session);
	}
	
	// Deselect all cells
	private void deselectAll(HttpSession session) {
		String selectable = "00000"
				+ "00000"
				+ "00000"
				+ "00000"
				+ "00000";	
		session.setAttribute("selectable", selectable);
	}	
	
	private void distributeCards(HttpSession session) {
		Character me = ((String) session.getAttribute("player_color")).charAt(0);
		Character opp;
		if (me=='r') {
			opp = 'b';
		} else {
			opp = 'r';
		}
		session.setAttribute(opp + "next", (int) session.getAttribute(me + "play"));
		session.setAttribute(me + "play", 0);
		if (((int) session.getAttribute(me+"opt1")) == 0) {
			session.setAttribute(me+"opt1", ((int)session.getAttribute(me+"next")));
			session.setAttribute(me+"next", 0);
		} else {
			session.setAttribute(me+"opt2", ((int)session.getAttribute(me+"next")));
			session.setAttribute(me+"next", 0);
		}
		session.setAttribute("player_turn", opp.toString());		
	}
	
	private void incrementGS(Connection connection,HttpSession session) {
		String game_name = (String) session.getAttribute("game_name");	
		RefreshServlet.incrementGameState(game_name);
	}	
	
	private String reverseString(String in) {
		StringBuilder out = new StringBuilder();
		for (int i = in.length()-1; i>=0; i--){
			out.append(in.charAt(i));
		}
		return out.toString();
	}
	
	private void resetGame(HttpSession session) {
		String game_name = (String) session.getAttribute("game_name");
		Connection connection = DBconnection.getConnectionToDatabase();
		RefreshServlet.game_state.replace((String)session.getAttribute("game_name"), 1);
		deselectAll(session);
		String board_pos = "bblbb"
					+ "nnnnn"
					+ "nnnnn"
					+ "nnnnn"
					+ "rrjrr";
		if(((String) session.getAttribute("player_color")).charAt(0) == 'b') {
			board_pos = (new StringBuilder(board_pos).reverse()).toString();
		}
		session.setAttribute("board_pos", board_pos);
		session.setAttribute("win", 0);				
		buildGameDeck(connection, session, game_name);
	}
}

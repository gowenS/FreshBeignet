package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/checkrefresh")
public class RefreshServlet extends HttpServlet{
	
	public static HashMap<String,Integer> game_state;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		HttpSession session = req.getSession();
		String gameName = (String) session.getAttribute("gameName");
		int game_sesh_st = (int) session.getAttribute("gameState");
		Boolean refreshNow = false;
		if (game_sesh_st != game_state.get(gameName)) {
//			System.out.println("session st: " + game_sesh_st + " in memory: " + game_state.get(gameName));
			refreshNow = true;
		}
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		if (refreshNow) {
			out.write("REFRESH");
		} else {
			out.write("");
		}		
	}
	
	public static void incrementGameState(String game_name) {
		if (game_state == null) {
			game_state = new HashMap<>();
		}
		if (game_state.containsKey(game_name)) {
			game_state.replace(game_name, (game_state.get(game_name)+1));
		} else {
			game_state.put(game_name, 0);
		}		
	}
	
	public static int getGameState(String game_name) {
		if (game_state == null) {
			game_state = new HashMap<>();
		}		
		if (game_state.containsKey(game_name)) {
			return game_state.get(game_name);
		} 
		return 0;
	}
}

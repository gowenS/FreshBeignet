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
		String game_name = (String) session.getAttribute("game_name");
		int game_sesh_st = (int) session.getAttribute("game_state");
		Boolean refreshNow = false;
		if (game_sesh_st != game_state.get(game_name)) {
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

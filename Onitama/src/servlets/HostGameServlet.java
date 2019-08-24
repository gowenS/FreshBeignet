package servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.HostDao;
import servlets.RefreshServlet;

@WebServlet("/host")
public class HostGameServlet extends HttpServlet {	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/html/hostgame.jsp").forward(req, resp);		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		String player_name = req.getParameter("player_name");
		int base = 0;
		int expansion = 0;
		Map<String,String[]> parameters = req.getParameterMap();
		if (parameters.containsKey("base")) base = 1;
		if (parameters.containsKey("expansion")) expansion = 1;
		HttpSession session = req.getSession();
		HostDao dao = new HostDao();
		if (base == 0 && expansion == 0) {
			session.setAttribute("options_error_code", "One of the game options must be selected to proceed.");
			session.setAttribute("player_name", null);
			req.getRequestDispatcher("html/hostgame.jsp").forward(req, resp);
		} else {
			session.setAttribute("options_error_code", null);
			String game_name = dao.createGame(player_name, session, base, expansion);
			session.setAttribute("game_name", game_name);
			session.setAttribute("player_color", "red");
			session.setAttribute("game_state", 0);
			RefreshServlet.incrementGameState(game_name);
			resp.sendRedirect("play");	
		}
	}
}

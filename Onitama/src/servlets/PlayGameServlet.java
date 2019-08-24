package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.PlayerDao;
@WebServlet("/play")
public class PlayGameServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		PlayerDao dao = new PlayerDao();
		HttpSession session = req.getSession();
		dao.getGameState(session);
		req.getRequestDispatcher("/html/playgame.jsp").forward(req, resp);		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		HttpSession session = req.getSession();
		PlayerDao dao = new PlayerDao();
		Map<String,String[]> parameters = req.getParameterMap();
		if (!parameters.containsKey("btnprs")) {
			int base = 0;
			int expansion = 0;
			if (parameters.containsKey("base")) base = 1;
			if (parameters.containsKey("expansion")) expansion = 1;
			if (base == 0 && expansion == 0) {
				session.setAttribute("options_error_code", "One of the game options must be selected to proceed.");
				resp.sendRedirect("play");
			} else {
				session.setAttribute("options_error_code", null);
				session.setAttribute("base", base);
				session.setAttribute("expansion", expansion);
				dao.buttonPress("new_game", session);
				resp.sendRedirect("play");
			}
		} else {
			dao.buttonPress(req.getParameter("btnprs"),session);	
		}		
	}		
}

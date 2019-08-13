package servlets;

import java.io.IOException;
import java.util.LinkedList;

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
		HttpSession session = req.getSession();
		HostDao dao = new HostDao();
		String game_name = dao.createGame(player_name, session);
		session.setAttribute("game_name", game_name);
		session.setAttribute("player_color", "red");
		session.setAttribute("game_state", 0);
		RefreshServlet.incrementGameState(game_name);
		resp.sendRedirect("play");	
	}
}

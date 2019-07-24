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
		session.setAttribute("player_name", player_name);
		HostDao dao = new HostDao();
		String gameName = dao.createGame(player_name, session);
		session.setAttribute("gameName", gameName);
		session.setAttribute("player_color", "red");
		req.getRequestDispatcher("/html/playgame.jsp").forward(req, resp);	
	}
}

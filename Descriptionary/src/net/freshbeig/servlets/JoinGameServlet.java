package net.freshbeig.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.freshbeig.dao.PlayerDao;

@WebServlet("/join")
public class JoinGameServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		req.getRequestDispatcher("/html/joingame.jsp").forward(req, resp);		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String gameNameAttempt = ((String) req.getParameter("game_name")).toUpperCase();
		String playerNameAttempt = (String) req.getParameter("player_name");
		PlayerDao dao = new PlayerDao();
		int code = dao.joinGame(gameNameAttempt, playerNameAttempt, session);
		switch(code) {
			case 1: 
				session.setAttribute("join_error_code", null);
				session.setAttribute("gameState", 0);
				resp.sendRedirect("playgame");
				break;
			case 2:
				session.setAttribute("join_error_code", "Error entering room code. Please try again.");
				req.getRequestDispatcher("/html/joingame.jsp").forward(req, resp);
				break;
			case 3:
				session.setAttribute("join_error_code", "The lobby for this game is already full.");
				req.getRequestDispatcher("/html/joingame.jsp").forward(req, resp);
				break;
			case 4:
				session.setAttribute("join_error_code", "This name has already been chosen. Please choose a different name.");
				req.getRequestDispatcher("/html/joingame.jsp").forward(req, resp);
				break;
			default:
				req.getRequestDispatcher("/html/joingame.jsp").forward(req, resp);
				break;
		}			
	}
}

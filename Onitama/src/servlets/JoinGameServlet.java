package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.PlayerDao;

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
		int code = dao.joinGame(gameNameAttempt, playerNameAttempt);
		switch(code) {
			case 1: 
				session.setAttribute("join_error_code", null);
				session.setAttribute("player_color", "blue");
				session.setAttribute("player_name", playerNameAttempt);
				resp.sendRedirect("playgame");
				break;
			case 2:
				session.setAttribute("join_error_code", "Error entering room code. Please try again.");
				session.setAttribute("player_color", null);
				session.setAttribute("player_name", null);
				req.getRequestDispatcher("/html/joingame.jsp").forward(req, resp);
				break;
			case 3:
				session.setAttribute("join_error_code", "This game is already full.");
				req.getRequestDispatcher("/html/joingame.jsp").forward(req, resp);
				break;
			default:
				req.getRequestDispatcher("/html/joingame.jsp").forward(req, resp);
				break;
		}			
	}
}

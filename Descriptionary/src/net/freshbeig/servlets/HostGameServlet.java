package net.freshbeig.servlets;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.freshbeig.dao.HostDao;

@WebServlet("/host")
public class HostGameServlet extends HttpServlet {	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getSession().getAttribute("numPlayers") == null) {
			HostDao dao = new HostDao();
			int[] player_lims = dao.getPlayerNumLims();
			HttpSession session = req.getSession();
			session.setAttribute("min_players", player_lims[0]);
			session.setAttribute("max_players", player_lims[1]);
			req.getRequestDispatcher("/html/hostoptions.jsp").forward(req, resp);
		} else {
			if (!(Boolean)req.getSession().getAttribute("gameStarted")) {
				HostDao dao = new HostDao();
				HttpSession session = req.getSession();
				LinkedList<String> playerNames = dao.getJoinedPlayers(session);
				session.setAttribute("playerNames", playerNames);
				session.setAttribute("numPlayersConnected", playerNames.size());
				req.getRequestDispatcher("/html/gamelobby.jsp").forward(req, resp);
			} else {
				HostDao dao = new HostDao();
				HttpSession session = req.getSession();
				int gameState = (int) session.getAttribute("game_state");
				int numPlayers = (int) session.getAttribute("numPlayers");
				if(gameState <= numPlayers) {
					LinkedList<Boolean> playersSubmitted = dao.getPlayersSubmitted(session);
					session.setAttribute("playersSubmitted", playersSubmitted);
					req.getRequestDispatcher("/html/runhostroom.jsp").forward(req, resp);
				} else {
					resp.sendRedirect("show_stacks");
				}				
			}			
		}		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getSession().getAttribute("numPlayers") == null) {
			int numPlayers = Integer.parseInt(req.getParameter("playerNum"));
			HttpSession session = req.getSession();
			session.setAttribute("numPlayers", numPlayers);
			HostDao dao = new HostDao();
			String gameName = dao.createGame(numPlayers, session);
			session.setAttribute("gameName", gameName);
			session.setAttribute("numPlayersConnected", 0);
			req.getRequestDispatcher("/html/gamelobby.jsp").forward(req, resp);
		} else {
			HttpSession session = req.getSession();
			session.setAttribute("gameStarted", true);
			HostDao dao  = new HostDao();
			dao.initializeGameHost((String) session.getAttribute("gameName"));
			session.setAttribute("game_state", 1);
			LinkedList<Boolean> playersSubmitted = dao.getPlayersSubmitted(session);
			session.setAttribute("playersSubmitted", playersSubmitted);
			req.getRequestDispatcher("/html/runhostroom.jsp").forward(req, resp);			
		}		
	}
}

package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.LobbyDao;

@WebServlet("/gamelobby")
public class GameLobbyServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		LobbyDao dao = new LobbyDao();
		int round = dao.getRound(session);
		if (round == 0) {
			dao.getTeams(session);
			req.getRequestDispatcher("/html/gamelobby.jsp").forward(req, resp);	
		} else {
			resp.sendRedirect("play");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		LobbyDao dao = new LobbyDao();
		dao.buttonPress(req.getParameter("btnprs"),session);
	}
	

}

package net.freshbeig.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.freshbeig.dao.PlayerDao;

@WebServlet("/playgame")
public class PlayGameServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();		
		if((int)session.getAttribute("gameState") <= (int) session.getAttribute("numPlayers")) { 
			PlayerDao dao = new PlayerDao();
			boolean refreshNow = dao.playerPlayingGame(session);
			session.setAttribute("refresh_now", refreshNow);
			req.getRequestDispatcher("/html/playgame.jsp").forward(req, resp);	
		} else {
			resp.sendRedirect("end");
		}			
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		PlayerDao dao = new PlayerDao();
		int myState = (int) session.getAttribute("myState");
		if((myState %2) != 0 ) {
			dao.placeDwgTurn(session, req);			
		} else {
			String textDesc = req.getParameter("text_description");
			session.setAttribute("text_description", textDesc);
			System.out.println((String) req.getAttribute("text_description"));
			dao.placeDescTurn(session);
		}
		session.setAttribute("myState",myState+1);
		session.setAttribute("refresh_now", true);
		req.getRequestDispatcher("/html/playgame.jsp").forward(req, resp);			
	}

}

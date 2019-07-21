package net.freshbeig.servlets;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.freshbeig.dao.SharedDao;

@WebServlet("/end")
public class EndGame extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		LinkedList<String> stack = new LinkedList<String>();
		SharedDao dao = new SharedDao();
		HttpSession session = req.getSession();
		stack = dao.getStack(session, (String)session.getAttribute("gameName"), (int) session.getAttribute("player_id"),(int)session.getAttribute("numPlayers"));
		session.setAttribute("stack", stack);
		req.getRequestDispatcher("/html/playerstack.jsp").forward(req, resp);	
	}
}

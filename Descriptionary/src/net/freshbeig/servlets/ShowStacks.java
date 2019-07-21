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

@WebServlet("/show_stacks")
public class ShowStacks extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SharedDao dao = new SharedDao();
		HttpSession session = req.getSession();
		String gameName = (String) session.getAttribute("gameName");
		if(session.getAttribute("stack_counter") == null) session.setAttribute("stack_counter", 1);
		int player = (int) session.getAttribute("stack_counter");
		int numPlayers = (int) session.getAttribute("numPlayers");
		if(player <= numPlayers) {
			LinkedList<String> stack = dao.getStack(session, gameName, player, numPlayers);
			session.setAttribute("stack", stack);
			session.setAttribute("stack_counter", player+1);
			req.getRequestDispatcher("/html/showstacks.jsp").forward(req, resp);
		} else {
			//end game goes here
		}		
	}
}

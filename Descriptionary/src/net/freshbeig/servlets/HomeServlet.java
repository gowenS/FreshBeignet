package net.freshbeig.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/home",""})
public class HomeServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().removeAttribute("numPlayers");
		req.getSession().removeAttribute("gameName");
		req.getSession().removeAttribute("stack_counter");
		req.getSession().removeAttribute("stack");
		req.getSession().setAttribute("gameStarted", false);
		req.getRequestDispatcher("/html/index.jsp").forward(req, resp);
	}
}

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

@WebServlet("/setplayerlimits")
public class SetPlayerLims extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/html/setplayerlimits.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		HttpSession session = req.getSession();
		int min_players = Integer.parseInt(req.getParameter("min_players"));
		int max_players = Integer.parseInt(req.getParameter("max_players"));;
		SharedDao dao = new SharedDao();
		dao.changePlayerNumLims(min_players,max_players);
		resp.sendRedirect("home");		
	}

}

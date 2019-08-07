package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.PlayerDao;
@WebServlet("/play")
public class PlayGameServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		PlayerDao dao = new PlayerDao();
		HttpSession session = req.getSession();
		dao.getGameState(session);
		req.getRequestDispatcher("/html/playgame.jsp").forward(req, resp);		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		HttpSession session = req.getSession();
		System.out.println(req.getParameter("btnprs"));
		req.getRequestDispatcher("/html/playgame.jsp").forward(req, resp);		
	}	
	
}

package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.PlayerDao;

@WebServlet("/play")
public class PlayGameServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		PlayerDao dao = new PlayerDao();
		dao.getGameState(session);
		int round_num = (int) session.getAttribute("round_num");
		if (round_num == 0) {
			resp.sendRedirect("gamelobby");
		} else {
			req.getRequestDispatcher("/html/playgame.jsp").forward(req, resp);	
		}		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		PlayerDao dao = new PlayerDao();
		Map<String,String[]> reqMap = req.getParameterMap();
		if (reqMap.containsKey("btnprs")) {
			dao.buttonPress(session, reqMap.get("btnprs")[0]);
		} 
		else if (reqMap.containsKey("clue_in")){
			dao.enterClue(session, reqMap.get("clue_in")[0], reqMap.get("clue_number")[0]);
			resp.sendRedirect("play");
		}
	}
}

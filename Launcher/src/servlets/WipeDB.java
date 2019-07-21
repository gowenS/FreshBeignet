package servlets;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DBconnection;

//Yes this is exactly as lazy as it looks.
@WebServlet("/performcleanup")
public class WipeDB extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection connection = DBconnection.getConnectionToDatabase();
		String curGame = "";
		String code = "";
        try{
            String sql = "select game_name from games";
            java.sql.PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet allGames = statement.executeQuery();
            while(allGames.next()){
                curGame = allGames.getString("game_name");
                sql = "drop table if exists " + curGame;
                statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                sql = "delete from games where game_name like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, curGame);
                statement.executeUpdate();
            }            
            sql = "select * from imagesfilepath";
            statement = connection.prepareStatement(sql);
            ResultSet directory = statement.executeQuery();
            directory.next();
            String fpath = directory.getString("path");
            File dir = new File(fpath);
            File[] listFiles = dir.listFiles();
            for(File file:listFiles) {
            	file.delete();
            }            
            code = "Successfully cleared Database";
        } catch(SQLException exception){        	
        	code = "Did not fully clear Database.";
            exception.printStackTrace();
        }        
        HttpSession session = req.getSession();
        session.setAttribute("clear_code", code);        
        req.getRequestDispatcher("html/wipedb.jsp").forward(req, resp);		
	}
}

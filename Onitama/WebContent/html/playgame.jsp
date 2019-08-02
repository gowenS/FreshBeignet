<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import = "classes.MoveCard" %>
<%!
Boolean my_turn;
String bg;
private String getMoveCardButton(Boolean turn, String card, HttpSession session) {
	int mc = (int)session.getAttribute(card);
	if (mc > 0) {
		MoveCard mv_card = new MoveCard(mc);
		String bg = mv_card.getBG();
	} else {
		
	}
	String out;
	if (turn) {
		out = "<input type= src=" + bg + " class=moveCard />";
	} else {
		out = "<img src=" + bg + " class=moveCard >";
	}
	return out;
}
%>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<%if ( ((String) session.getAttribute("player_turn")==null) || ( ((String)session.getAttribute("player_color")).charAt(0)) != (((String)session.getAttribute("player_turn")).charAt(0)) ) {
			my_turn = false;%>  
		<meta http-equiv="refresh" content=2>
		<% } else {
			my_turn = true;
		}%>
		<title>Onitama | Fresh Beignet</title>
		<link rel="stylesheet" href="css/style.css">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	</head>
<body>
	<h3>Room: <%=(String)session.getAttribute("game_name") %></h3>
	<h2><%=(String)session.getAttribute("blue")%></h2>
	<%=getMoveCardButton(my_turn, "bnext", session)%>
	<%=getMoveCardButton(my_turn, "bopt2", session)%>
	<%=getMoveCardButton(my_turn, "bopt1", session)%>
	<%=getMoveCardButton(my_turn, "bplay", session)%>
	<h3><%=(String) session.getAttribute("player_turn")%></h3>
	<%=getMoveCardButton(my_turn, "rplay", session)%>
	<%=getMoveCardButton(my_turn, "ropt1", session)%>
	<%=getMoveCardButton(my_turn, "ropt2", session)%>
	<%=getMoveCardButton(my_turn, "rnext", session)%>
	<h2><%=(String) session.getAttribute("red") %></h2>
</body>
</html>
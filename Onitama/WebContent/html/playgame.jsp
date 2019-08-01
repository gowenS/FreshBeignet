<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import = "classes.MoveCard" %>
<%
MoveCard ropt1 = new MoveCard((int)session.getAttribute("ropt1"));
MoveCard ropt2 = new MoveCard((int)session.getAttribute("ropt2"));
MoveCard bopt1 = new MoveCard((int)session.getAttribute("bopt1"));
MoveCard bopt2 = new MoveCard((int)session.getAttribute("bopt2"));
MoveCard rnext = new MoveCard((int)session.getAttribute("rnext"));
MoveCard bnext = new MoveCard((int)session.getAttribute("bnext"));
MoveCard rplay = new MoveCard((int)session.getAttribute("rplay"));
MoveCard bplay = new MoveCard((int)session.getAttribute("bplay"));
%>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<%if ( ((String) session.getAttribute("player_turn")==null) || ( ((String)session.getAttribute("player_color")).charAt(0)) != (((String)session.getAttribute("player_turn")).charAt(0)) ) {
			Boolean my_turn = false;%>  
		<meta http-equiv="refresh" content=2>
		<% } else {
			Boolean my_turn = true;
		}%>
		<title>Onitama | Fresh Beignet</title>
		<link rel="stylesheet" href="css/style.css">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	</head>
<body>
	<h3>Room: <%=(String)session.getAttribute("game_name") %></h3>
	<h2><%=(String)session.getAttribute("blue")%></h2>
	<h3><%=(String) session.getAttribute("player_turn")%></h3>
	<h2><%=(String) session.getAttribute("red") %></h2>
</body>
</html>
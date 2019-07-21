<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import = "java.util.LinkedList" %>
<%@ page import = "java.util.ListIterator" %>
<html>
<head>
<meta charset="ISO-8859-1">
<!-- Only refresh page if lobby is not full -->
<%if (((int)session.getAttribute("numPlayers"))>((int)session.getAttribute("numPlayersConnected"))) {%>  
<meta http-equiv="refresh" content=4>
<% } %>
<title>Fresh Beignet | Waiting for all to join</title>
<link rel="stylesheet" href="css/style.css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<h3>You have configured a room for <%=(int)session.getAttribute("numPlayers")%> players.</h3>
	<h3><%=(int)session.getAttribute("numPlayersConnected")%> players have joined the game using room code </h3> 
	
	<h2><b><mark><%=(String)session.getAttribute("gameName") %></mark></b></h2>
	
	<div class=center></div>
	<% if(((int)session.getAttribute("numPlayersConnected")) > 0){
	LinkedList<String> playerNames = (LinkedList<String>)session.getAttribute("playerNames");
	ListIterator<String> playerNameIterator = playerNames.listIterator(0);
	while (playerNameIterator.hasNext()) {%>
		<h3><%=playerNameIterator.next() %></h3>
		
		
	<%}}%>
	
	<%if (((int)session.getAttribute("numPlayers"))==((int)session.getAttribute("numPlayersConnected")) && !((Boolean) session.getAttribute("gameStarted"))) {%>  
		<form action="host" method="post">
			<button class=button1 autofocus>Start Game.</button>
		</form>		
	<% } %>
	
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import = "java.util.LinkedList" %>
<%@ page import = "java.util.ListIterator" %>
<html>
<head>
<meta charset="ISO-8859-1">
<meta http-equiv="refresh" content=3>
<title>Fresh Beignet</title>
<link rel="stylesheet" href="css/style.css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<h3><%=(String) session.getAttribute("gameName") %></h3>
	<%if((int) session.getAttribute("game_state")%2 != 0) { %>
		<!-- Drawing Phase -->
		<%if(((int) session.getAttribute("game_state")) == 1) { %>
			<h3>Draw whatever you want.</h3>
		<%} else { %>
			<h3>Draw what was described.</h3>
			
		<%} %>
		
	<%} else { %>
		<!-- Writing Phase -->
		<h3>Describe what was drawn.</h3>
	
	
	<%} %>
	<div class="table">
	<table>
		<% LinkedList<String> playerNames = (LinkedList<String>)session.getAttribute("playerNames");
		ListIterator<String> playerNameIterator = playerNames.listIterator(0);
		LinkedList<Boolean> playerSub = (LinkedList<Boolean>)session.getAttribute("playersSubmitted");
		ListIterator<Boolean> playerSubIterator = playerSub.listIterator(0);	
		while(playerNameIterator.hasNext()) {%>
			<tr>
				<td><% if(playerSubIterator.next()){%>
					<img src="images/submitted.png" alt="submitted" class=submittedbox>
					<%} else { %>
					<img src="images/not_submitted.png" alt="not submitted" class=submittedbox>
					<%} %></td>
				<td><%=playerNameIterator.next() %></td>
			</tr>
		
		<%} %>
	</table>
	</div>
</body>
</html>
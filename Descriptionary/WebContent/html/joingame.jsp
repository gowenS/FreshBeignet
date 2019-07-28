<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Fresh Beignet | Join Game</title>
<link rel="stylesheet" href="css/style.css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<div class="content">
	
		<form action="join" method="post">
			<h3>Enter your name.</h3>		
			<input type="text" name="player_name" maxlength="12" class=joinTextField>
			<h3>Enter the room code from the game host.</h3>
			<input type="text" name="game_name" maxlength="4" style="text-transform:uppercase" class=joinTextField>
			<button type="submit" class=button1>Join the game.</button>	
		</form>
		<%if((String) session.getAttribute("join_error_code") != null) { %>
			<h5><%=(String) session.getAttribute("join_error_code") %></h5>
		<%} %>
	</div>
</body>
</html>
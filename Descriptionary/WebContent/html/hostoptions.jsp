<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%int minPlayers = 6;
int maxPlayers = 15; %>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Fresh Beignet | Configure your room, Host!</title>
		<link rel="stylesheet" href="css/style.css">
  		<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<h3>How many players are you hosting?</h3>	
	<div class=center></div>
	<form action="host" method="post">
		<select name="playerNum" size="1" class=dropdown>
			<%for(int i = minPlayers; i <= maxPlayers; i++) { %>
				<option value="<%=i%>"><%=i%></option>				
			<%} %>
		</select>
		<div class=center></div>
		<button type="submit" class=button1>Start hosting the game</button>
	</form>	

</body>
</html>
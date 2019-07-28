<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Descriptionary | Fresh Beignet | Set Player Limits</title>
		<link rel="stylesheet" href="css/style.css">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	</head>
	<body>
		<form action="setplayerlimits" method="post">

			<h3>Enter Minimum number of Players</h3>		
			<input type="number" id="min_players" name="min_players" value="6" min="1" max="15" >
			<h3>Enter Maximum number of Players</h3>		
			<input type="number" id="max_players" name="max_players" value="15" min="10" max="15" >
			<button type="submit" class=button1>Enter the new numbers</button>	
		</form>

	</body>
</html>
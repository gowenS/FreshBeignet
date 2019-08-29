<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Fresh Beignet | Host Game</title>
<link rel="stylesheet" href="css/style.css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<div class="content">
		<form action="host" method="post">
			<h3>Enter your name.</h3>		
			<input type="text" name="player_name" maxlength="12" class=joinTextField>
 			<button type="submit" class=button1>Host the game.</button>	
		</form>	
	</div>
</body>
</html>
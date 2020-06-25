<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Welcome, enjoy the freshest of all the beignets here.</title>
		<link rel="stylesheet" href="css/style.css">
  		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	</head>
	<body>
		<h2>Welcome to Fresh Beignet!</h2>
		<img src="images/fblogo.png" alt="Freshest Beignet" class=center >
		<form action="Codenames" method="get">
			<button class=button1>Play Codenames</button>
		</form> 
		<form action="Onitama" method="get">
			<button class=button1>Play Onitama</button>
		</form> 	
		<form action="Descriptionary" method="get">
			<button class=button1>Play Descriptionary</button>
		</form> 			
		<div class=center></div>
		<h6>More game options coming soon!</h6>	
		<form action="reconnect" method="get">
			<button class=button2>Reconnect to an in-progress game.</button>
		</form> 	
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Game Lobby | Fresh Beignet</title>
	<link rel="stylesheet" href="css/style.css">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<h3>Room Code:<%=(String) session.getAttribute("gameName")%></h3>
	<h3><%=(String) session.getAttribute("playerName")%></h3>



	<script>
		window.setInterval(checkRF, 1000);
		function checkRF() {
			var refreshCheck = new XMLHttpRequest();
			refreshCheck.open('POST', 'checkrefresh', true);
			refreshCheck.setRequestHeader('Content-Type',
					'application/x-www-form-urlencoded');
			refreshCheck.onload = function() {
				if (refreshCheck.response === "REFRESH") {
					location.reload();
				}
			}
			refreshCheck.send(null);
		}
	</script>
</body>
</html>
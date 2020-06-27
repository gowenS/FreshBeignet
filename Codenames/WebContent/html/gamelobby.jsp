<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!

private String showTeam(String team) {
	StringBuilder out = new StringBuilder("\n\n\n");
	out.append(team.replace(",","\n\n"));
	return out.toString();	
}

%>

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
	<div class="lobbyTextAreaHolder">
		<textarea class="lobbyTextAreaRed" readonly>RED TEAM<%= showTeam((String)session.getAttribute("redTeam")) %></textarea>
		<textarea class="lobbyTextAreaGray" readonly>Join a Team<%= showTeam((String)session.getAttribute("noTeam")) %></textarea>
		<textarea class="lobbyTextAreaBlue" readonly>BLUE TEAM <%= showTeam((String)session.getAttribute("blueTeam")) %></textarea>
	</div>
	
	<div class="buttonholder">
		<button onclick="sendBtnClick('r')" class=buttonRed>Join Red</button>	
		<button onclick="sendBtnClick('b')" class=buttonBlue>Join Blue</button>
	</div>
	
	<div class="buttonholder">
		<% if (((String)session.getAttribute("noTeam")).isEmpty() && !((String)session.getAttribute("redTeam")).isEmpty() && !((String)session.getAttribute("blueTeam")).isEmpty()) {%>
		<button onclick="sendBtnClick('p')" class=button1>Start Game</button>
		<%} %>
	</div>
	
	<script>
		function sendBtnClick(btn){
			var xhr = new XMLHttpRequest();
			xhr.open('POST','gamelobby',true);
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			console.log(btn);
			xhr.send("btnprs=" + btn);
			window.setTimeout(function(){ location.reload(); },250);
		}
	</script>
	
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
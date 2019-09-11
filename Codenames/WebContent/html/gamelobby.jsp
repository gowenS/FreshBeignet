<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!

private String showTeam(String team) {
	StringBuilder out = new StringBuilder("\n");
	out.append(team.replace(",","\n"));
	return out.toString();	
}

//TODO only show play game button once enough players are distributed across teams
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
		<button onclick="sendBtnClick('p')" class=buttonBlue>Start Game</button>
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
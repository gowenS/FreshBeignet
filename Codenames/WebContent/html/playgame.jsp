<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!
String me;
String myColor;
String opponentColor;
String phase;
private String getBtnCls(String clr) {
	return "";
}

%>

<html>
<head>
	<meta charset="UTF-8">
	<title>Codenames | Fresh Beignet</title>
</head>
<body>
	<% 
	me = (String) session.getAttribute("player_color");
	if (me.equals("r")) {
		myColor = "Red";
		opponentColor = "Blue";
	} else{
		myColor = "Blue";
		opponentColor = "Red";
	}	
	phase = (String)session.getAttribute("game_phase");
	if (phase.equals("v")) { //choose spies%>
		<h3>Room Code: <%=(String) session.getAttribute("gameName") %></h3>
		<h3>Choose the spies</h3>
		<div class="lobbyTextAreaHolder">
			<textarea class="lobbyTextAreaSRed" readonly><%= (String)session.getAttribute("spy_red_name") %></textarea>
			<textarea class="lobbyTextAreaBlue" readonly><%= (String)session.getAttribute("spy_blue_name") %></textarea>
		</div>
		<div class="buttonholder">
			<button onclick="sendBtnClick('v')" class=button<%=getBtnCls(myColor) %>>Become Spy</button>
		</div>
	<%} else { //regular game %>
		<h3>Hey its a game now</h3>
	<%}%>
	
	<script>
		function sendBtnClick(btn){
			var xhr = new XMLHttpRequest();
			xhr.open('POST','play',true);
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
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import = "classes.WordList,java.util.ArrayList,java.util.Arrays,java.util.Collections"%>
<%!
// Codenames
String me;
String myColor;
String opponentColor;
String phase;
ArrayList<String> wordsArray;
Boolean iAmSpy;
Boolean myTurn;
String turn;

private String fullColorName(String color) {
	String out = "blue";
	if (color.charAt(0) == 'r') {
		out = "red";
	}
	return out;
}

private String oppColor(String color) {
	String out = "r";
	if (color.charAt(0) == 'r') {
		out = "b";
	}
	return out;
}

private String getGameBoard(Boolean iAmSpy, Boolean myTurn, String game_phase, HttpSession session, ArrayList<String> wordsArray) {
	StringBuilder out = new StringBuilder("<br/> \n");
	String revealed = (String) session.getAttribute("revealed");
	String board_colors = (String) session.getAttribute("board_colors");
	String selected = (String) session.getAttribute("selected");
	String clue = ((String) session.getAttribute("clue")).strip();
	String pic = "k";
	String sel = "";
	for (int i = 0; i<25; i++){
		sel = selected.charAt(i) == '1' ? "s" : "";
		if(iAmSpy || (revealed.charAt(i) == '1' || game_phase.charAt(0) != 'p')){
			pic = board_colors.charAt(i)+"";
		} else {
			pic = "k" + sel;
		}
		if ( i % 5 == 0){
			out.append("<br/>");
		} 
		if (myTurn && !iAmSpy && pic.contains("k") && clue != "" && game_phase.charAt(0) == 'p') {
			out.append("<button onclick=\"sendBtnClick('" + Integer.toString(i) + "')\" class=gridCell"+pic+">" + wordsArray.get(i) + "</button> \n");
		} else {
			if (iAmSpy && (revealed.charAt(i) == '1') && game_phase.charAt(0) == 'p') {
				out.append("<button class=gridCell"+pic+">--</button> \n");
			} else {
				out.append("<button class=gridCell"+pic+">" + wordsArray.get(i) + "</button> \n");
			}			
		}
	}	
	out.append("<br/> \n");
	return out.toString();
}

private String getWinningTeam(char w_l, String turn) {
	String out;
	if (w_l == 'w') {
		out = fullColorName(turn);
	} else{
		out = fullColorName(oppColor(turn));
	}
	return out;
}



%>

<html>
<head>
	<meta charset="UTF-8">
	<title>Codenames | Fresh Beignet</title>
	<link rel="stylesheet" href="css/style.css">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<% 
	me = (String) session.getAttribute("player_color");
	if (me.equals("r")) {
		myColor = "red";
		opponentColor = "blue";
	} else{
		myColor = "blue";
		opponentColor = "red";
	}	
	phase = (String)session.getAttribute("game_phase");
	WordList wordString = new WordList((String)session.getAttribute("words"));
	wordsArray = wordString.getCurrentGameArray();
	if ((int)session.getAttribute("my_ID") == (int)session.getAttribute("spy_"+myColor)) {
		iAmSpy = true;
	} else {
		iAmSpy = false;
	}
	turn = (String) session.getAttribute("turn");
	myTurn = turn.equals(me);
	
	if (phase.equals("v")) { 
		
		//choose spies%>
		<h3>Room Code: <%=(String) session.getAttribute("gameName") %></h3>
		<h3>Choose the spies</h3>
		<div class="lobbyTextAreaHolder">
			<textarea class="spyTextAreaRed" readonly><%= ((String)session.getAttribute("spy_red_name"))==null?"":((String)session.getAttribute("spy_red_name")) %></textarea>
			<textarea class="spyTextAreaBlue" readonly><%= ((String)session.getAttribute("spy_blue_name"))==null?"":((String)session.getAttribute("spy_blue_name")) %></textarea>
		</div>
		<div class="buttonholder">
			<button onclick="sendBtnClick('v')" class=button1>Become <%= fullColorName(me) %> Spy</button>
		</div>
	<%} else { //regular game  
	
	%>
		<h3>Room Code: <%=(String) session.getAttribute("gameName") %></h3>
		<h3>Red Spy: <%=(String) session.getAttribute("spy_red_name") %> | Blue Spy: <%= (String) session.getAttribute("spy_blue_name") %></h3>
		<h3><%= fullColorName((String) session.getAttribute("turn")) %> Turn</h3>
		<%= getGameBoard(iAmSpy, myTurn, phase, session, wordsArray) %>
		<% if (iAmSpy && myTurn && ((String)session.getAttribute("clue")).strip() == ""){%>
			<form id="clue_form" action="play" method="post">			
				<h3>Enter your hint</h3>		
				<input type="text" name="clue_in" maxlength="50" class=joinTextField>
				<h3>Enter you hint number</h3>
				<select name="clue_number" size="1" class=dropdown>
					<%for(int i = 0; i <= 10; i++) { %>
						<option value="<%=i%>"><%=i%></option>				
					<%} %>
				</select>				
			</form>
			<button onclick="sendBtnClick('submit_clue')" class=button1>Submit your hint.</button>	
		<%} %>
		<% if (((String)session.getAttribute("clue")).strip() != ""){ %>
			<h3>Hint: <%=(String)session.getAttribute("clue") %>, <%=(int)session.getAttribute("clue_number") %></h3>
			<% if (!iAmSpy && myTurn && phase.charAt(0) == 'p') {  %>
				<% if (((String) session.getAttribute("selected")).contains("1")) { %>
				<button onclick="sendBtnClick('submit_selected')" class=button1>Submit Selection.</button>
				<% } %>
				<button onclick="sendBtnClick('end_turn')" class=button1>End Turn.</button>
			<%} %>
		<%} %>
		<% if (phase.charAt(0) == 'l' || phase.charAt(0) == 'w') { %>
			<h3><%= getWinningTeam(phase.charAt(0), turn) %> Team Wins.</h3>
			<button onclick="sendBtnClick('play_again')" class=button1>Play again.</button>
			<button onclick="sendBtnClick('change_teams')" class=button1>Change Teams.</button>
		<%} %>
	<%}%>
	
	<script>
		function sendBtnClick(btn){
			if (btn === "submit_clue") {
				document.getElementById("clue_form").submit();
			} else {
				var xhr = new XMLHttpRequest();
				xhr.open('POST','play',true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				console.log(btn);
				xhr.send("btnprs=" + btn);	
				window.setTimeout(function(){ location.reload(); },250);
			}					
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
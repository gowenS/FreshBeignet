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
private String getBtnCls(String clr) {
	return "";
}

private String fullColorName(String color) {
	String out = "blue";
	if (color.charAt(0) == 'r') {
		out = "red";
	}
	return out;
}

private String getGameBoard(Boolean iAmSpy, Boolean myTurn, HttpSession session, ArrayList<String> wordsArray) {
	StringBuilder out = new StringBuilder("<br/> \n");
	String revealed = (String) session.getAttribute("revealed");
	String board_colors = (String) session.getAttribute("board_colors");
	String selected = (String) session.getAttribute("selected");
	String clue = ((String) session.getAttribute("clue")).strip();
	String pic = "n";
	String sel = "";
	for (int i = 0; i<25; i++){
		sel = selected.charAt(i) == '1' ? "s" : "";
		if(iAmSpy || (revealed.charAt(i) == '1')){
			pic = board_colors.charAt(i)+"";
		} else {
			pic = "n" + sel;
		}
		if ( i % 5 == 0){
			out.append("<br/>");
		} 
		if (myTurn && !iAmSpy && pic.contains("n") && clue != "") {
			out.append("<button onclick=\"sendBtnClick('" + Integer.toString(i) + "')\" class=gridCell"+pic+">" + wordsArray.get(i) + "</button> \n");
		} else { 
			out.append("<button class=gridCell"+pic+">" + wordsArray.get(i) + "</button> \n");
		}
	}	
	out.append("<br/> \n");
	return out.toString();
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
	Boolean myTurn = ((String) session.getAttribute("turn")).equals((String)session.getAttribute("player_color"));
	
	if (phase.equals("v")) { 
		
		//choose spies%>
		<h3>Room Code: <%=(String) session.getAttribute("gameName") %></h3>
		<h3>Choose the spies</h3>
		<div class="lobbyTextAreaHolder">
			<textarea class="lobbyTextAreaRed" readonly><%= (String)session.getAttribute("spy_red_name") %></textarea>
			<textarea class="lobbyTextAreaBlue" readonly><%= (String)session.getAttribute("spy_blue_name") %></textarea>
		</div>
		<div class="buttonholder">
			<button onclick="sendBtnClick('v')" class=button1>Become Spy</button>
		</div>
	<%} else { //regular game  
	
	%>
		<h3>Room Code: <%=(String) session.getAttribute("gameName") %></h3>
		<h3>Red Spy: <%=(String) session.getAttribute("spy_red_name") %> | Blue Spy: <%= (String) session.getAttribute("spy_blue_name") %></h3>
		<h3><%= fullColorName((String) session.getAttribute("turn")) %> Turn</h3>
		<%= getGameBoard(iAmSpy, myTurn, session, wordsArray) %>
		<% if (iAmSpy && myTurn && ((String)session.getAttribute("clue")).strip() == ""){%>
			<form action="play" method="post">			
				<h3>Enter your hint</h3>		
				<input type="text" name="clue" maxlength="50" class=joinTextField>
				<h3>Enter you hint number</h3>
				<select name="clue_number" size="1" class=dropdown>
					<%for(int i = 0; i <= 7; i++) { %>
						<option value="<%=i%>"><%=i%></option>				
					<%} %>
				</select>
				<button type="submit" class=button1>Submit your hint.</button>	
			</form>
		<%} %>
		<% if (((String)session.getAttribute("clue")).strip() != ""){ %>
			<h3>Hint: <%=(String)session.getAttribute("clue") %>, <%=(int)session.getAttribute("clue_number") %></h3>
			<% if (!iAmSpy && myTurn) {  %>
				<button onclick="sendBtnClick('submit_selected')" class=button1>Submit Selections.</button>
			<%} %>
		<%} %>
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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import = "classes.MoveCard" %>
<%!
// Onitama
Boolean my_turn;
String me;
String opponent;
Boolean win = false;

private String getMoveCardButton(Boolean turn, String card, HttpSession session, Boolean flip) {
	int mc = (int)session.getAttribute(card);
	String bg = "";
	String flipped = "";
	if (flip) {
		flipped = "-flipped";
	}
	if (mc != 0) {
		MoveCard mv_card = new MoveCard(mc);
		if (card.substring(card.length()-4).equals("next")) {
			bg = mv_card.getBGinv();
		} else {
			bg = mv_card.getBG();
		}
	} else {
		if (card.charAt(0)=='r'){
			bg = "images/redcard.png";
		} else {
			bg = "images/bluecard.png";	
		}		
	}
	String out;
	if (turn && !card.contains("next") && !flip && mc!=0) {  // The only Move Cards which are buttons are those which belong to the player whose turn it is and are not empty
		out = "<button onclick=\"sendBtnClick('" + card + "')\" class=moveCardCellHolder > <img src=" + bg + " class=moveCard" + flipped +" ></button>";
	} else {
		out = "<img src=" + bg + " class=moveCard" + flipped + " >";
	}
	return out;
}

private String getGameBoard(Boolean turn, HttpSession session) {
	StringBuilder out = new StringBuilder("<br/> \n");
	String board_pos = (String) session.getAttribute("board_pos");
	String selectable = (String) session.getAttribute("selectable");
	String highlight = (String) session.getAttribute("highlight");
	String player_color = (String) session.getAttribute("player_color");
	Boolean invert = false;
	String flipped = "";
	int num;
	if (player_color.equals("blue")) {
		invert = true;
		flipped = "-flipped";
	}
	for (int i = 0; i<25; i++){
		if ( i % 5 == 0){
			out.append("<br/>");
		} 
		if (my_turn) {
			out.append("<button onclick=\"sendBtnClick('" + Integer.toString(i) + "')\" class=gridCellHolder ><img src=" + getGridCellImgSource(board_pos.charAt(i),selectable.charAt(i),highlight.charAt(i)) + " class=gridCell" + flipped +" ></button> \n");
		} else { 
			out.append("<img src=" + getGridCellImgSource(board_pos.charAt(i),selectable.charAt(i),highlight.charAt(i)) + " class=gridCell" + flipped + " > \n");
		}
	}	
	out.append("<br/> \n");
	return out.toString();
}

private String getGridCellImgSource(Character pos, Character selected, Character highlighted) {
	StringBuilder out = new StringBuilder("images/");
	switch (pos) {
		case 'n':
			out.append("blankcell");
			break;
		case 'b':
			out.append("pawnblue");
			break;
		case 'l':
			out.append("masterblue");
			break;
		case 'r':
			out.append("pawnred");
			break;
		case 'j':
			out.append("masterred");
			break;
		case 'q':
			out.append("platformred");
			break;
		case 'w':
			out.append("platformblue");
	}
	if (selected == '1'){
		out.append("selectable");
	}
	out.append(".png");
	return out.toString();
}

private String reverseString(String in) {
	StringBuilder out = new StringBuilder();
	for (int i = in.length()-1; i>=0; i--){
		out.append(in.charAt(i));
	}
	return out.toString();
}
%>
<html>
	<head>
		<meta charset="ISO-8859-1">		
		<title>Onitama | Fresh Beignet</title>
		<link rel="stylesheet" href="css/style.css">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	</head>
<body>
	<% 
	me = (String) session.getAttribute("player_color");
	if (me.equals("red")) {
		opponent = "blue";
	} else{
		opponent = "red";
	}		
	if ( ((String) session.getAttribute("player_turn")==null) || ( ((String)session.getAttribute("player_color")).charAt(0)) != (((String)session.getAttribute("player_turn")).charAt(0)) ) {
		my_turn = false;
	} else {
		my_turn = true;
	}
	if((int) session.getAttribute("win") == 1){
		win = true;
	} else {
		win = false;
	}
	%>
	<h3>Room: <%=(String)session.getAttribute("game_name") %></h3>
	<h3><% if (!my_turn){ %>--<% } %>
	<%=(String)session.getAttribute(opponent)%>
	<% if(!my_turn) { %>--<% }%></h3>	
	<%=getMoveCardButton(my_turn, opponent.charAt(0) + "next", session, true)%>
	<%=getMoveCardButton(my_turn, opponent.charAt(0) + "opt2", session, true)%>
	<%=getMoveCardButton(my_turn, opponent.charAt(0) + "opt1", session, true)%>
	<%=getMoveCardButton(my_turn, opponent.charAt(0) + "play", session, true)%>
	<%=getGameBoard(my_turn, session) %>
	<%=getMoveCardButton(my_turn, me.charAt(0) + "play", session, false)%>
	<%=getMoveCardButton(my_turn, me.charAt(0) + "opt1", session, false)%>
	<%=getMoveCardButton(my_turn, me.charAt(0) + "opt2", session, false)%>
	<%=getMoveCardButton(my_turn, me.charAt(0) + "next", session, false)%>
	<h3><% if (my_turn){ %>--<% } %>
	<%=(String)session.getAttribute(me)%>
	<% if(my_turn) { %>--<% } %></h3>
	<% if (win) { %>
		<form action="play" method="post">	
			<input type="checkbox" name="base" value="1" <%= ((((int) session.getAttribute("base")) == 1) ? "checked" : "" )%>>Base Game<br>
			<input type="checkbox" name="expansion" value="1" <%= ((((int) session.getAttribute("expansion")) == 1) ? "checked" : "" )%>>Expansion<br>
 			<button type="submit" class=button1>Play Again</button>	
		</form>
		<%if((String) session.getAttribute("options_error_code") != null) { %>
			<h5><%=(String) session.getAttribute("options_error_code") %></h5>
		<%} %>
	<% } %>
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
	<% if ((!my_turn) || (win)) { %>
	<script>
		window.setInterval(checkRF,400);
		function checkRF(){
			var refreshCheck = new XMLHttpRequest();
			refreshCheck.open('POST','checkrefresh',true);
			refreshCheck.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			refreshCheck.onload = function () {
				if (refreshCheck.response === "REFRESH") {
					location.reload();
				}
			}
			refreshCheck.send(null);
		}
	</script>
	<% } %>
</body>
</html>
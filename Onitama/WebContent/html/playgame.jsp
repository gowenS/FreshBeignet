<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import = "classes.MoveCard" %>
<%!
Boolean my_turn;
String me;
String opponent;

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
	if (turn) {
		out = "<button onclick=\"sendBtnClick('" + card + "')\" class=moveCard > <img src=" + bg + " class=moveCard" + flipped +" ></button>";
	} else {
		out = "<img src=" + bg + " class=moveCard" + flipped + " >";
	}
	return out;
}
%>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<%me = (String) session.getAttribute("player_color");
		if (me.equals("red")) {
			opponent = "blue";
		} else{
			opponent = "red";
		}		
		if ( ((String) session.getAttribute("player_turn")==null) || ( ((String)session.getAttribute("player_color")).charAt(0)) != (((String)session.getAttribute("player_turn")).charAt(0)) ) {
			my_turn = false;%>  
		<meta http-equiv="refresh" content=2>
		<% } else {
			my_turn = true;
		}%>
		<title>Onitama | Fresh Beignet</title>
		<link rel="stylesheet" href="css/style.css">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	</head>
<body>
	<h3>Room: <%=(String)session.getAttribute("game_name") %></h3>
	<h2><%=opponent %></h2>
	<h2><%=(String)session.getAttribute(opponent)%></h2>
	<%=getMoveCardButton(my_turn, opponent.charAt(0) + "next", session, true)%>
	<%=getMoveCardButton(my_turn, opponent.charAt(0) + "opt2", session, true)%>
	<%=getMoveCardButton(my_turn, opponent.charAt(0) + "opt1", session, true)%>
	<%=getMoveCardButton(my_turn, opponent.charAt(0) + "play", session, true)%>
	<h3><%=(String) session.getAttribute("player_turn")%></h3>
	<%=getMoveCardButton(my_turn, me.charAt(0) + "play", session, false)%>
	<%=getMoveCardButton(my_turn, me.charAt(0) + "opt1", session, false)%>
	<%=getMoveCardButton(my_turn, me.charAt(0) + "opt2", session, false)%>
	<%=getMoveCardButton(my_turn, me.charAt(0) + "next", session, false)%>
	<h2><%=(String) session.getAttribute(me) %></h2>
	<h2><%=me %></h2>
	<script>
		function sendBtnClick(btn){
			var xhr = new XMLHttpRequest();
			xhr.open('POST','play',true);
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			console.log(btn);
			xhr.send("btnprs=" + btn);
		}
	</script>
</body>
</html>
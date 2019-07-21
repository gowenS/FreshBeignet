<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<!-- Only refresh page if ahead of rest of game server -->
<%if ((boolean)session.getAttribute("refresh_now")) {%>  
<meta http-equiv="refresh" content=4>
<% } %>
<title>Fresh Beignet | Let's play a game.</title>
<link rel="stylesheet" href="css/style.css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style>

canvas {
    margin: auto;
    display: block;
    border: 1px solid black;
    
}

</style>
</head>
<body>
	<h3><%=(String) session.getAttribute("myName") %></h3>
	<%if ((boolean)session.getAttribute("refresh_now")) {%>  
	<img src="images/waiting.jpg" alt="Freshest Beignet" class=center >
	<% } else {%>
		<h5>Turn <%=(int) session.getAttribute("myState") %></h5>
		
		<%if (((int) session.getAttribute("myState") % 2) != 0 ) {  %>
			<!-- What to do for drawing turn -->
			
				<%if ((int) session.getAttribute("myState") !=1) {  %>
					<h5>Draw this: <%=(String) session.getAttribute("text_description") %></h5>
				<%} %>
			<div class="dwgOptionsButtonHolder">
				<button onclick="changeLineWeight('fine')" class=dwgOptionButton>Fine</button>
				<button onclick="changeLineWeight('medium')" class=dwgOptionButton>Medium</button>
				<button onclick="changeLineWeight('bold')" class=dwgOptionButton>Bold</button>
				<button onclick="changeLineColor('black')" class=dwgBlackButton></button>
				<button onclick="changeLineColor('red')" class=dwgRedButton></button>
				<button onclick="changeLineColor('blue')" class=dwgBlueButton></button>
				<button onclick="changeLineColor('green')" class=dwgGreenButton></button>
				<button onclick="changeLineColor('yellow')" class=dwgYellowButton></button>
			</div>
			<div>
				<canvas id="canvas" width="355" height="440"></canvas>
				
						
			</div>
			<div class="buttonholder">
				<button onclick="deleteLine()" class=button4>Undo</button>
				<button type="submit" class=button3 form=submit_form>Submit</button>
			</div>
			
			<form action="playgame" method="post" onsubmit="prepareImg();" id="submit_form">
				<input id="submittedimg" name="img_from_player" type="hidden" value=""> 						
			</form>
			
			<script>				
				function prepareImg(){
					var canvas = document.getElementById('canvas');
					document.getElementById('submittedimg').value = canvas.toDataURL();
				}				
			</script>
		
		<%} else { %>
			<!-- What to do for writing turn -->
			<!--  replace this image with image from server -->
			<img src="/gameimgs/<%=(String) session.getAttribute("img_path") %>" alt="What is drawn?" class=centerpic >
			<div>
				<h5>What was drawn?</h5>
				<textarea name="text_description" maxlength="300" form="submit_form" autofocus></textarea>
			</div>
						
			<!-- Submit button -->
			<form action="playgame" method="post" id="submit_form">
				<button type="submit" class=button2>Submit</button>			
			</form>
					
		<% } %>
		
	<%} %>
	<script src="html/canvas.js"></script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import = "java.util.LinkedList" %>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Fresh Beignet | Player <%=(int) session.getAttribute("player_id") %></title>
	<link rel="stylesheet" href="css/style.css">
  	<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<h3>Progression for <%= (String) session.getAttribute("myName") %></h3>
	<%String this_name;
	for (int i = 1; i <= ((int) session.getAttribute("numPlayers"));i++) { %>
		<%LinkedList<String> stack = (LinkedList<String>) session.getAttribute("stack");
		if (i % 2 != 0) { %>
		<!-- Drawings -->
			<%this_name = stack.remove();%>
			<h6>Drawn by <%=this_name %></h6>
			<img src="/gameimgs/<%=stack.remove()%>" alt="What was drawn by <%=this_name %>." class=centerpic >
		<%} else { %>
		<!-- Text Descriptions -->
			<h6>Written by <%=stack.remove() %></h6>
			<h5><%=stack.remove() %></h5>
		<% } %>		
	
	<% } %>
	
	<form action="home" method="get">
		<button class=button1>End of Game. Go Home.</button>
	</form>	
	
</body>
</html>
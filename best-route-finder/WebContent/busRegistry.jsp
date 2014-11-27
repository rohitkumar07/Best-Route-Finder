<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Bus Route Finder</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"></link>
<link rel="stylesheet" type="text/css" href="css/style.css"></link>

<script src="js/jquery-latest.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</head>
<body>
		
		<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	      <div class="container">
	        <div class="navbar-header">
	          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
	            <span class="sr-only">Toggle navigation</span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </button>
	          <a class="navbar-brand" href="#">Bus Route Finder</a>
	        </div>
	        <form action="mainServlet" method="post">
	        <div id="navbar" class="collapse navbar-collapse">
	          <ul class="nav navbar-nav navbar-right">
	          	<li><a href="guest.jsp">Home</a></li>
	          	 <li><a href="guest.jsp#about">About</a></li>
	          	<% 
					if(session!= null && session.getAttribute("username")!=null){
				%> 
						<li class="dropdown">
			              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"> <%=session.getAttribute("username") %> <span class="caret"></span></a>
			              
				              <ul class="dropdown-menu" role="menu">
				                	<li><a href="bus.jsp">Bus registry</a></li>			                			                													                					
									<li><a href="#" onclick="$(this).closest('form').submit()">Sign out</a></li>								
									<input type="hidden" name="pageid" VALUE="logout">																							
				              </ul>			           
			            </li>	
				<%	} else { %>
						<li><a href='#' data-toggle="modal" data-target="#LoginModal">Login/Sign Up</a></li>								
				<% 
					}
				%>			            					           
	          </ul>
	        </div><!--/.nav-collapse -->
	        </form>
	      </div>
	    </nav>
		<div class="container">
			<form  action="mainServlet" method="post">
				<h2>Bus Registry</h2>	
				<label for="BusID">Bus ID :</label> 
				<input type="text" class="form-bus" name="BusID" placeholder="Bus ID" data-html="true" data-content="" data-placement="right" title="" autofocus><br>	
				<div class= "radio">
							Select Type 
							<label>
				            	<input type="radio" name="type" value="AC"> AC
				          	</label>
				          	<label>
				            	<input type="radio" name="type" value="NA"> Non-AC
				          	</label>
						</div> 
				<label for="first_stop" class="">Route :</label> 						
				<input type="text" id="last-stop" class="bus-stop form-bus" name="last_bus_stop" placeholder="First Stop" data-html="true" data-content="" data-placement="right" title="" autofocus>				
				<input type="hidden" name="pageid" VALUE="bus_registry">
				<button class="btn btn-lg btn-success" id="next_bus_stop" type="button">Next Stop</button><br><br>
				<button class="btn btn-lg btn-primary">Submit</button>
			</form> 
		</div>
		

</body>
</html>
		
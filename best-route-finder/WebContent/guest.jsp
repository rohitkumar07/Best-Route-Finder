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
	          <a class="navbar-brand" href="guest.jsp">Bus Route Finder</a>
	        </div>
	        <form action="mainServlet" method="post">
	        <div id="navbar" class="collapse navbar-collapse">
	          <ul class="nav navbar-nav navbar-right">
	          	<li class="active"><a href="#">Home</a></li>
	          	 <li><a href="#about">About</a></li>
	          	<% 
					if(session!= null && session.getAttribute("username")!=null){
				%> 
						<li class="dropdown">
			              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"> <%=session.getAttribute("username") %> <span class="caret"></span></a>
			              
				              <ul class="dropdown-menu" role="menu">
				                	<li><a href="busRegistry.jsp">Bus registry</a></li>			                			                													                					
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
	    <div class="modal fade" id="PayModal" tabindex="-1" role="dialog" aria-labelledby="login" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-body" id="PayModalBody">
		      		      	
		      </div>
		    </div>
		  </div>
		</div>
	    <div class="modal fade" id="LoginModal" tabindex="-1" role="dialog" aria-labelledby="login" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-body">
		        <div role="tabpanel">
				  <ul class="nav nav-tabs" role="tablist">
				    <li role="presentation" class="active"><a href="#login" aria-controls="login" role="tab" data-toggle="tab">Login</a></li>
				    <li role="presentation"><a href="#signup" aria-controls="signup" role="tab" data-toggle="tab">Sign Up</a></li>
				  </ul>
				
				  <!-- Tab panes -->
				  <div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="login">				    	
						<div>
							<form class="form-signin" action="mainServlet" method="post">
								<h2 class="form-signin-heading">Login</h2>								
								<label for="username" class="sr-only">Username :</label> 
								<input type="text"  name="username" class="form-control" placeholder="Username" autofocus>				
								<label for="pwd" class="sr-only">Password :</label> 
								<input type="password" name="pwd" class="form-control" placeholder="Password" autofocus>
								<input type="hidden" name="pageid" VALUE="login"><br>
								<button class="btn btn-lg btn-primary btn-block" id="submit" type="submit">Submit</button>
							</form> 
						</div>
				    </div>
				    <div role="tabpanel" class="tab-pane" id="signup">
				    	<div>
							<form class="form-signin" action="mainServlet" method="post">
								<h2 class="form-signin-heading">Sign Up</h2>
								<label for="username" class="sr-only">Username :</label> 
								<input type="text"  name="username" class="form-control" placeholder="Username" autofocus>				
								<label for="pwd" class="sr-only">Password :</label> 
								<input type="password" name="pwd" class="form-control" placeholder="Password" autofocus>
								<label for="confirmpwd" class="sr-only">Confirm Password :</label> 
								<input type="password" name="confirmpwd" class="form-control" placeholder="Confirm Password" autofocus>
								<input type="hidden" name="pageid" VALUE="signup"><br>
								<button class="btn btn-lg btn-primary btn-block" id="submit" type="submit">Submit</button>
							</form> 
						</div>
				    </div>
				  </div>
				
				</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		      </div>
		    </div>
		  </div>
		</div>
		<div class="container">
				<% if(session!= null && session.getAttribute("message")!=null){
							System.out.println("message: " + session.getAttribute("message")); 
				%>
					<div class="alert alert-danger">
						<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>				
				        <a href="#" class="close" data-dismiss="alert">&times;</a>				
				        <strong>Error!</strong> <%= session.getAttribute("message") %>			
				    </div>
				<%		session.invalidate();}
				%>								   

			<div class="row">
				<div id="hide-this" class="col-md-4">					
					<% 
						if(session!= null && session.getAttribute("username")!=null && session.getAttribute("fav_routes") != null){
							HashMap fav_routes = (HashMap <String,String>)session.getAttribute("fav_routes");
					%>
						<ul class="list-group fav-routes">
						<a class="list-group-item active">
						    Favourite Routes
						</a>
					<%


							//access via new for-loop
							for( Object key: fav_routes.keySet()) {
								
							    Object value = fav_routes.get(key);
							    String src = (String) key;
							    String dst = (String) value;							
							
								%>
									  <a href="#" class="list-group-item fav-route" src="<%=src %>" dst="<%=dst %>">
									 		<%=src %> -> <%=dst %>
									 </a>
								<%
							}
						}
					%>	
					</ul>
					<script>
						$(document).ready(function(){
							$('.fav-route').click(function(event){
								$("#src").val($(this).attr("src"));
								$("#dstn").val($(this).attr("dst"));								
							});
						});
					</script>
				
				</div>
				<div class="col-md-4">
					<form class="form-signin" >
						<h2 class="form-signin-heading">Search for buses</h2>
						<label for="src" class="sr-only">Enter Source :</label> 
						<input type="text" id="src" name="src" class="form-control bus-stop" placeholder="Source Bus Stop" data-html="true" data-content="" data-placement="right" title=""  autofocus>				
						<label for="dstn" class="sr-only">Enter Destination :</label> 
						<input type="text" id="dstn" name="dstn" class="form-control bus-stop" placeholder="Destination Bus Stop" data-html="true" data-content="" data-placement="right" title="" autofocus>
						<input type="text" id="time" name="time" class="form-time" placeholder="Time of travel (HH:MM)" autofocus>	
  							<select class="form-time" id="am_pm">
   								<option>AM</option>
    							<option>PM</option>
  							</select>				
						<div class= "radio">
							Select Type 
							<label>
				            	<input type="radio" name="type" value="AC"> AC
				          	</label>
				          	<label>
				            	<input type="radio" name="type" value="NA"> Non-AC
				          	</label>
						</div>  
						<div class=""></div>
						<input type="hidden" name="pageid" VALUE="search_bus">
						<button type="button" class="btn btn-lg btn-primary btn-block" id="bus-search-button"><span class="glyphicon glyphicon-search"></span>&nbsp;Search Buses</button>
					</form> 
				</div>
				<div id="search-results" class="col-md-8">
				</div>	
			 </div>
		</div>
		

</body>
</html>
		
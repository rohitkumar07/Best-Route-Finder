package main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class signUp {

	public void doSignUp(HttpServletRequest request, HttpServletResponse response) throws
	ServletException, IOException, SQLException{
		
		String userName = request.getParameter("username");
        String password = request.getParameter("pwd");
        String confirmPassword = request.getParameter("confirmpwd");
        
        ResultSet rs = null;
		rs = mainServlet.st.executeQuery(" select * from bus_users where username = " + "'" + userName + "';");
        
    	if (mainServlet.conn1 != null && rs != null){
        	String invalidLogin = "";
        	HttpSession mySession = request.getSession(true);
        	
        	if (rs.next()){
        		mySession.setAttribute("message","username already exists"  );    
        	}
        	else if(!password.equals(confirmPassword)){
        		mySession.setAttribute("message","Passwords don't Match"  ); 
        	}
        	else{
        		System.out.println("insert into bus_users (username, password) values ('" + userName + "', '" + password + "');");
        		mainServlet.st.executeUpdate("insert into bus_users (username, password) values ('" + userName + "', '" + password + "');");
        		mySession.setAttribute("username", userName);
        	}
    	}
    	response.sendRedirect("/best-route-finder/guest.jsp");
    	if (rs != null){
    		rs.close();
    	}
	}
}

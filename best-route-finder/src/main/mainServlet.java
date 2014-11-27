package main;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.security.Principal;

 
/**
 * This program demonstrates how to make database connection to PostgreSQL
 * server using JDBC.
 *
 *
 */

public class mainServlet extends HttpServlet {

	/**
	 * 
	 */
	// Version Check
	private static final long serialVersionUID = 1L;
	public static Connection conn1 =null;
	public static Statement st =null;
	
	loginCheck newLoginCheck = new loginCheck();
	signUp newSignUp = new signUp();
	transaction newTransactionPage = new transaction();
	LogoutHandler logoutHandler = new LogoutHandler();
	
	public void init() throws ServletException {
	      //Open the connection here
		
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
		String user = "rohit";
        String pass = "golbat";

        try {
			Class.forName("org.postgresql.Driver");
		
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			System.out.println(conn1.toString());
			st = conn1.createStatement();
			//System.out.println("init"+conn1);
        	} catch (Exception e) {
			// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
	    }

	    public void destroy() {
	     //Close the connection here
	    	try{
	    		conn1.close();
	    		System.out.println("close");
	    	}catch(Exception e)
	    	{
	    		System.out.println(e);
	    	}
	    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
	ServletException, IOException
	{
        
        //Connection conn1 = null;
        String pageId = request.getParameter("pageid");
        
        try {
	            
	            if (pageId.equals("login")){
	            	newLoginCheck.getLogin(request, response);
	            }
	            
	            else if (pageId.equals("signup")){
	            	newSignUp.doSignUp(request, response);
	            }
	            
	            
	            else if (pageId.equals("search_bus")){
	            	newTransactionPage.doTransaction(request, response);
	            }
	            
	            else if(pageId.equals("logout")) {
	            	logoutHandler.logThisOut(request,response);
	            }
	            else if(pageId.equals("next_stop")) {
	            	busRegistry.passNextStops(request,response);
	            }
	            else if(pageId.equals("bus_registry")) {
	            	busRegistry.doBusRegistry(request, response);
	            }
	  
            }             
         catch (SQLException ex) {
        	 System.out.println(" sql exception present ");
            ex.printStackTrace();
            //response.sendRedirect("/project1/Intro.html");
        }
    }
}
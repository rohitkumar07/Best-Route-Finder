package main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AjaxHandler
 */
@WebServlet("/AjaxHandler")
public class AjaxHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	Connection conn1 =null;
	Statement st =null;
	
	public void init() throws ServletException {
      //Open the connection here
	
		String dbURL2 = "jdbc:postgresql://localhost/cs387";
	    String user = "rohit";
	    String pass = "golbat";
	
	    try {
			Class.forName("org.postgresql.Driver");
		
			conn1 = DriverManager.getConnection(dbURL2, user, pass);
			st = conn1.createStatement();
			//System.out.println("init "+conn1);
	    	} catch (Exception e) {
			// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	}
    }

	public void destroy() {
	 //Close the connection here
		try{
			conn1.close();
			//System.out.println("close");
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
	ServletException, IOException
	{
        
        //Connection conn1 = null;
		System.out.println("ajax");
        String name= request.getParameter("name");
        String src = request.getParameter("src");
        String dst = request.getParameter("dst");
        System.out.println("src");
        String retval = "";
        String retval1 = "";
        String retval2 = "";
        String result1= "";
        if(name!=null && !name.equals("")){
	        try {
	            
	            ResultSet rs;            
	            rs = st.executeQuery("SELECT * from bus_stops where bus_stop_name like '" + name +"%'");
	            if (conn1 != null) {
	                while(rs.next()) {
	                    retval = rs.getString(1);
	                    retval1 = rs.getString(2);
	                    retval2 = rs.getString(3);
	                    if(result1.equals(""))
	                    	result1 = retval1;
	                    else
	                    	result1=result1+","+retval1;
	                    
	                    //System.out.println(retval1);
	                }
	                rs.close();
	                response.setContentType("text/plain");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(result1);
	            }
	 
	            
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	
	        }
        } else if(src!=null && !src.equals("")) {
        	String srcID = "";
        	String dstID = "";
        	HttpSession mySession = request.getSession(true);
        	if(mySession!=null && mySession.getAttribute("username") != null) {
	        	String username = (String) mySession.getAttribute("username");
	        	try {
					ResultSet rs = st.executeQuery("SELECT * from bus_stops where bus_stop_name ='" + src + "';" );
					if (conn1 != null && rs.next()) {
						srcID = rs.getString(1);
					}
					rs = st.executeQuery("SELECT * from bus_stops where bus_stop_name ='" + dst + "';" );
					if (conn1 != null && rs.next()) {
						dstID = rs.getString(1);
					}
					st.executeUpdate("  UPDATE bus_users set (fav_src,fav_dst) = (fav_src||'{" + srcID + "}',fav_dst||'{"+ dstID +"}') where username='"+ username + "';");	
					HashMap<String, String> fav_routes = (HashMap<String, String>) mySession.getAttribute("fav_routes");
					fav_routes.put(src, dst);
					mySession.setAttribute("fav_routes",fav_routes);
					response.setContentType("text/plain");
		            response.setCharacterEncoding("UTF-8");
		            response.getWriter().write("success");
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}       
        	}
        }
    }

}

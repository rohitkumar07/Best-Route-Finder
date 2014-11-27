package main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class loginCheck {

	public void getLogin (HttpServletRequest request, HttpServletResponse response) throws
	ServletException, IOException, SQLException{
		String userName = request.getParameter("username");
		
        String password = request.getParameter("pwd");
		
        ResultSet rs = null;
		
    	rs = mainServlet.st.executeQuery("select password from bus_users where username ='" + userName + "';");
    	//System.out.println("select password from bus_users where username ='" + userName + "';");
    	if (mainServlet.conn1 != null && rs != null){
    		String resString = "";
    		while(rs.next()){
    			resString = rs.getString(1);
    		}
    		
    		System.out.println("password entered: " +  password);
    		HttpSession mySession = request.getSession(true);
    		if (resString.equals(password)){    			    			
    			mySession.setAttribute("username", userName);	    				
	    				ArrayList<String> favSource = new ArrayList<String>();
	    				ArrayList<String> favDestination = new ArrayList<String>();
	    				ArrayList<String> registryList = new ArrayList<String>();
	    				HashMap<String, Integer> alreadyInsertedIDs = new HashMap<String, Integer>();
	    				mySession.setAttribute("alreadyInsertedIDs", alreadyInsertedIDs);
	    				mySession.setAttribute("busRegistryList", registryList);
	    				
	    				String favSrcQuery = "select unnest(fav_src) from bus_users where username = '" + userName +"'; ";
	    				rs = mainServlet.st.executeQuery(favSrcQuery);
	    				if (mainServlet.conn1 != null) {
	    					while(rs.next()) {
	    						favSource.add(rs.getString(1));	    						
	    					}
	    				}
	    				rs.close();
	    				
	    				String favDstQuery = "select unnest(fav_dst) from bus_users where username = '" + userName +"'; ";
	    				rs = mainServlet.st.executeQuery(favDstQuery);
	    				if (mainServlet.conn1 != null) {
	    					while(rs.next()) {
	    						favDestination.add(rs.getString(1));	    						
	    					}
	    				}
	    				rs.close();
	    				
	    					
	    				HashMap<String,String> fav_routes = new HashMap<String,String>();
	    				for (int i = 0; i < favSource.size(); i++){
	    					System.out.println(favSource.get(i) + "->" + favDestination.get(i));
	    					String busStopNameQuery = "select bus_stop_name from bus_stops where bus_stop_id = '" + favSource.get(i) + "';";
	    					rs = mainServlet.st.executeQuery(busStopNameQuery);
	    					String sourceName = "";
		    				String destinationName = "";	
	    					if (mainServlet.conn1 != null){
	    						if (rs.next()) {
	    							sourceName = rs.getString(1);
	    						}
	    					}
	    					rs.close();
	    					
	    					busStopNameQuery = "select bus_stop_name from bus_stops where bus_stop_id = '" + favDestination.get(i) + "';";
	    					rs = mainServlet.st.executeQuery(busStopNameQuery);
	    					
	    					if (mainServlet.conn1 != null){
	    						if (rs.next()) {
	    							destinationName =  rs.getString(1);
	    							
	    						}
	    					}
	    					rs.close();
	    					if(!(sourceName.equals("") || destinationName.equals(""))) {
	    						fav_routes.put(sourceName, destinationName);
	    					}
	    					
	    				}
	    				mySession.setAttribute("fav_routes", fav_routes);
	    				
	    				// sourceName and destinationName are the fav src-dstn pair
    		}
    		else if(!resString.equals("")){
    			mySession.setAttribute("message","Wrong Password."  );    			
    		} else {
    			mySession.setAttribute("message","Invalid Username."  );
    		}    		
    		response.sendRedirect("/best-route-finder/guest.jsp");
    	}
	}
}

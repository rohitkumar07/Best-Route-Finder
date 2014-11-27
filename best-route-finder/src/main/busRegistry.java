package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.interfaces.RSAKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.spi.DirStateFactory.Result;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;

public class busRegistry {
		
	
	public static ArrayList<String> getAdjacencyArrayList(String busStopName, HashMap<String, Integer> alreadyInsertedIDs ) throws SQLException{
		String busStopIdQuery = "select bus_stop_id from bus_stops where bus_stop_name = '" + busStopName + "';";
		ResultSet rs = null;
		rs = mainServlet.st.executeQuery(busStopIdQuery);
		
		String busStopId = null;
		if (mainServlet.conn1 != null){
			if (rs.next()) {
				busStopId = rs.getString(1);
			}
		}
		rs.close();
		alreadyInsertedIDs.put(busStopId, 0);
		
		String adjacencyQuery = "select dst_id from paths where src_id = '" + busStopId + "'";

		rs = mainServlet.st.executeQuery(adjacencyQuery);
		
		ArrayList<String> result = new ArrayList<String>();
		
		if (mainServlet.conn1 != null){
			while (rs.next()) {
				result.add( rs.getString(1));
			}
		}
		rs.close();
		
		ArrayList<String> adjacencyList = new ArrayList<String>(); 
		for (int i = 0; i < result.size(); i++){
			if (!alreadyInsertedIDs.containsKey(result.get(i))){
				String busStopNameQuery = "select bus_stop_name from bus_stops where bus_stop_id = '" + result.get(i) + "';";
				rs = mainServlet.st.executeQuery(busStopNameQuery);
				
				if (mainServlet.conn1 != null){
					if (rs.next()) {
						adjacencyList.add( rs.getString(1) );
					}
				}
				rs.close();
				
			}
		}
		return adjacencyList;
	}
	
	public static void doBusRegistry(HttpServletRequest request, HttpServletResponse response) throws
	ServletException, IOException{
		
		HttpSession mySession = request.getSession(true);
		@SuppressWarnings("unchecked")
		HashMap<String, Integer> alreadyInsertedIDs = (HashMap<String, Integer>) mySession.getAttribute("alreadyInsertedIDs"); 
		alreadyInsertedIDs.clear();
		
		String busID = request.getParameter("BusID");
		String bus_type = request.getParameter("type");
		String last_bus_stop = request.getParameter("last_bus_stop");
		ArrayList<String> registryList = null;
		if(last_bus_stop != ""){
			registryList = (ArrayList<String>) mySession.getAttribute("registry_list");
			registryList.add(last_bus_stop);
			ArrayList<String> registryIdList = new ArrayList<String>();
			for(String bus_stop_name : registryList){
				String idQuery = "select bus_stop_id from bus_stops where bus_stop_name  = '" + bus_stop_name + "'";

				try {
					ResultSet rs = mainServlet.st.executeQuery(idQuery);
					if (rs!= null && rs.next()) {
						registryIdList.add( rs.getString(1));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			mySession.removeAttribute("registry_list");
			System.out.println("inserting bus" + busID + " " +bus_type + " " + registryIdList.toString());
			//System.out.println("registrylist:" + registryList.toString() );
			/** default times*/String timeList = "Array[time '1:00 PM',time '2:00 PM',time '4:00 PM', time '5:00 PM']";
			String bus_stop_list = registryIdList.toString().substring(1, registryIdList.toString().length()-1);
			System.out.println(bus_stop_list);
			String routeQuery = "select route_id from routes where list_bus_stops = '{" + bus_stop_list + "}';";			
			
			int result = 0;
			try {
				ResultSet rs = mainServlet.st.executeQuery(routeQuery);
				String route_id = "";
				if (rs!= null && rs.next()) {
					route_id =  rs.getString(1);
				}
				if(route_id.equals("")){
					rs = mainServlet.st.executeQuery("select max(route_id) from routes;");
					if (rs!= null && rs.next()) {
						String max =  rs.getString(1);
						Integer temp = Integer.parseInt(max) + 1;
						route_id = temp.toString();
					}
					result = mainServlet.st.executeUpdate("insert into routes values ('" + route_id + "','" + registryIdList.get(0) + "','"  + registryIdList.get(registryIdList.size()-1) + "','{"  + bus_stop_list +"}') ");
					if(result <= 0){
						route_id = "";
					}
				}
				if (!bus_type.equals("") && !busID.equals("") && !route_id.equals("")){
					//System.out.println("insert into buses(bus_id,bus_type,route_id, timings) values ('" + busID + "','" + bus_type + "','" +route_id+"', " + timeList + ") ");
					result = mainServlet.st.executeUpdate("insert into buses(bus_id,bus_type,route_id, timings) values ('" + busID + "','" + bus_type + "','" +route_id+"', " + timeList + ") ");
				}
				if(result > 0) {
					response.sendRedirect("/best-route-finder/guest.jsp");
				} else {
					response.sendRedirect("/best-route-finder/busRegistry.jsp");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				response.sendRedirect("/best-route-finder/busRegistry.jsp");
				e.printStackTrace();
			}		
			
		}
		
		//alreadyInsertedIDs = new HashMap<String, Integer>();
    }
	
	public static void passNextStops(HttpServletRequest request, HttpServletResponse response) {
		HttpSession mySession = request.getSession(true);
		@SuppressWarnings("unchecked")
		String bus_stop = (String) request.getParameter("src");
		System.out.println(bus_stop);
		HashMap<String, Integer> alreadyInsertedIDs = (HashMap<String, Integer>) mySession.getAttribute("alreadyInsertedIDs"); 		
		try {
			
			ArrayList<String> nextStops = getAdjacencyArrayList(bus_stop, alreadyInsertedIDs);
			//mySession.setAttribute("alreadyInsertedIDs", alreadyInsertedIDs);
			JSONArray jsonArray = new JSONArray();
			for(String stop:nextStops) {
				jsonArray.put(stop);
			}
			if(jsonArray.length() != 0){
				if(mySession != null && mySession.getAttribute("registry_list") == null){
					ArrayList<String> registryList = new ArrayList<String>();
					registryList.add(bus_stop);
					mySession.setAttribute("registry_list",registryList);			
				} else if(mySession != null){
					@SuppressWarnings("unchecked")
					ArrayList<String> registryList = (ArrayList<String>) mySession.getAttribute("registry_list");
					registryList.add(bus_stop);
					mySession.setAttribute("registry_list",registryList);
				}
			}
			System.out.println(nextStops.toString());
			response.setContentType("application/json");
	    	response.setHeader("Cache-Control", "nocache");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out = response.getWriter();    

			// finally output the json string		
			out.print(jsonArray.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class transaction {

	public void doTransaction (HttpServletRequest request, HttpServletResponse response) throws
	ServletException, IOException, SQLException{
		HttpSession thisSession = request.getSession(true);
		boolean isGuest = (thisSession.getAttribute("username")==null );
		String userName = null;
		
		
        String source = request.getParameter("src");
        String destination = request.getParameter("dstn");
        String busTypeString = request.getParameter("type");
        String timeRequestString  = request.getParameter("time");
        
        Integer hourString = Integer.parseInt(timeRequestString.substring(0,  timeRequestString.indexOf(":")));
        timeRequestString = timeRequestString.substring(timeRequestString.indexOf(":") + 1);
        Integer minuteString = Integer.parseInt(timeRequestString.substring(0,2));
        Long defaultTime = (long) ((minuteString + (60*hourString) )*60);;
        if (timeRequestString.contains("PM")){
        	defaultTime += (12*60*60);
        }
        
        HashMap<String, String[]> routeToListHashMap = new HashMap<String, String[]>();

        @SuppressWarnings("deprecation")
		Time defaulTimeVal = new Time(0, 0, 0);
        Long offset = defaulTimeVal.getTime();
        
        System.out.println(defaultTime + "\n\n\n");
        
        boolean busType; // true means AC bus
        if (busTypeString.equals("AC")) {
        	busType = true;
        }
        else {
        	busType = false;
        }
        
        ResultSet rs = null;
      //rs = st.executeQuery(" select list_buses from bus_stops where bus_stop_name = '" + source + "';");
    	
        String availableBuses = "select unnest(list_buses) from bus_stops where bus_stop_name = '" + source + 
        		"' intersect select unnest(list_buses) from bus_stops where bus_stop_name = '" + destination + "';";
    	rs = mainServlet.st.executeQuery(availableBuses);
    	
    	ArrayList<String> busList = new ArrayList<String>();
    	
    	if (mainServlet.conn1 != null && rs != null){
    		String resString = "";
    		while(rs.next()){
    			resString = rs.getString(1);
    			busList.add(resString);
    			System.out.println(resString);
    		}
    	}
    	rs.close();
    	String responseText = "";
    	JSONArray jsonArray = new JSONArray();
    	for (int i = 0; i < busList.size(); i++){
    		System.out.println( "Processing bus ID: " + busList.get(i));
    		String routeIdQuery = "select route_id, timings from buses where bus_id = '" + busList.get(i) + "';";	
    		rs = mainServlet.st.executeQuery(routeIdQuery);
    		
    		String routeId = null;
			Time[] timings = null;
			
    		if (mainServlet.conn1 != null){
    			if(rs.next()){
        			routeId = rs.getString(1);
        			timings = (Time[])(rs.getArray(2)).getArray();        			
        		}
    		}
    		
			rs.close();
			String[] busStopIdList = null;
			
			if (routeToListHashMap.containsKey(routeId)){
				busStopIdList = routeToListHashMap.get(routeId);
			}
			else {
				String busStopListQuery = "select list_bus_stops from routes where route_id = '" + routeId + "';";
				rs = mainServlet.st.executeQuery(busStopListQuery);
				if (mainServlet.conn1 != null){
					if(rs.next()){
						busStopIdList = (String[])(rs.getArray(1)).getArray();
					}
				}
				rs.close();
			}
			
			
			String sourceIdQuery = "select bus_stop_id from bus_stops where bus_stop_name = '" + source + "';";
			String destinationIdQuery = "select bus_stop_id from bus_stops where bus_stop_name = '" + destination + "';";
			
			rs = mainServlet.st.executeQuery(sourceIdQuery);
			String sourceID = null, destinationID = null;
			if (mainServlet.conn1 != null){
				if(rs.next()){
					sourceID = rs.getString(1);
				}
			}
			rs.close();
			
			rs = mainServlet.st.executeQuery(destinationIdQuery);
			if (mainServlet.conn1 != null){
				if(rs.next()){
					destinationID = rs.getString(1);
				}
			}
			rs.close();

			/**
			 * total journey time, starting time of journey, 
			 */
			
			boolean startCounting = false;
			
			Long arrivalTime = (long) 0;
			Long journeyTime = (long) 0;
			Float totalCost = (float) 0;
			Long reachingTime = null;
			
			for (int j = 0; j < busStopIdList.length; j++ ) {
				String pathQuery = "select * from paths where src_id = '" + busStopIdList[j] + 
						"' and dst_id = '" + busStopIdList[j+1] + "';";
				
				rs = mainServlet.st.executeQuery(pathQuery);
				
				Time pathTime = null;
				Double[] trafficRateList = null;
				
				Float localCost = null;
				
				if (mainServlet.conn1 != null){
					if(rs.next()){
						pathTime = rs.getTime(6);
						trafficRateList = (Double[])(rs.getArray(7)).getArray();
						if (busType){
							localCost = rs.getFloat(4);
						}
						else {
							localCost = rs.getFloat(5);
						}
					}
				}
				rs.close();
				
				
				if (busStopIdList[j].equals(sourceID)){
					
					for (int timeIndex = 0; timeIndex < timings.length; timeIndex++){
						Long tempTime = ((timings[timeIndex]).getTime() - offset)/1000 + arrivalTime;
						if ( defaultTime >= tempTime && defaultTime < tempTime + 3600){
							startCounting = true;
							reachingTime = tempTime;
							break;
						}
					}
					
				}
				
				int index = (int) (defaultTime / 7200);
				Long actualPathTime = (long) ( (pathTime.getTime() - offset) * trafficRateList[index])/1000;
				// System.out.println(actualPathTime + " actual path time");
				defaultTime += actualPathTime;
				
				if (startCounting){
					journeyTime+=actualPathTime;
					totalCost+=localCost;
				}
				else {
					arrivalTime += actualPathTime;
				}
				
				if (busStopIdList[j+1].equals(destinationID) || busStopIdList[j].equals(destinationID)){
					break;
				}
			}
			if (reachingTime != null){
				Integer discountPercentage = 0;
				if (!isGuest){
					rs = mainServlet.st.executeQuery("select total_time from bus_users where username = '" + userName + "';");
					int totalTime = 0;
					if (mainServlet.conn1 != null){
						if (rs.next()){
							totalTime = rs.getInt(1);
						}
					}
					rs.close();
					
					Float discountFactor = (float) 1;
					if (totalTime > 36000){
						discountFactor = (float) (totalTime/36000);
						discountFactor = 1/(1+discountFactor);
					}
					totalCost = (float) ((0.7 * totalCost) + ((0.3 * totalCost) * discountFactor) );  
					discountPercentage = (int) (30 * (1 - discountFactor));
				}
				Integer hh = (int) (reachingTime / 3600);
				Integer mm = (int) ((reachingTime % 3600) / 60) ;				
				String minutes = mm.toString();
				if(mm<10){
					minutes = "0" + minutes;
				}
				Integer jh = (int) (journeyTime / 3600);
				Integer jm = (int) ((reachingTime % 3600) / 60);
				String jminutes = jm.toString();
				responseText += '\n' + totalCost +"\t" + discountPercentage + '\t' + jh + ':' + jminutes + '\t'+ busList.get(i) + '\t' + hh + ':' + minutes;
				String jtime = jh.toString()+":"+jminutes;
				String atime = hh.toString() + ':' + minutes;
				JSONObject json = new JSONObject();
		        try {
					json.put("totalCost", totalCost.toString());
					json.put("discountPercentage", discountPercentage.toString());		        
					json.put("journeyTime", jtime);
					json.put("busId", busList.get(i).toString());
					json.put("arrivalTime", atime );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		        
				jsonArray.put(json);
		        //System.out.println(json.toString());
			}
    			
    	}
    	response.setContentType("application/json");
    	response.setHeader("Cache-Control", "nocache");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();    

		// finally output the json string		
		out.print(jsonArray.toString());
		//System.out.println(jsonArray.toString());
    	rs.close();		
    }
}
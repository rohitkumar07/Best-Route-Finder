package main;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutHandler {
	public void logThisOut(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession mySession = request.getSession(true);
		mySession.invalidate();
		response.sendRedirect("/best-route-finder/guest.jsp");
	}
}

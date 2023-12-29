package com.vastpro.onlineexamapplication.event;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.webapp.control.LoginWorker;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class Logout {
	
	public static String logout(HttpServletRequest request,HttpServletResponse response) {
		boolean isUserLogin = false;
		request.getSession().getAttribute(OnlineExam.USERLOGIN);
		String logout = LoginWorker.logout(request, response);
		if(logout.equalsIgnoreCase("success")) {
		 isUserLogin= CheckLoginFlag.isUserlogin=true;
			
				request.setAttribute("isUserLogin", isUserLogin);
				return OnlineExam.SUCCESS;
		}
		String errMsg="user not logedOut";
		request.setAttribute("errMsg", errMsg);
		return OnlineExam.ERROR;
	}
}

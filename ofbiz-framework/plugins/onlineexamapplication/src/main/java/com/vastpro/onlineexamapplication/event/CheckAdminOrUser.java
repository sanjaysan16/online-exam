package com.vastpro.onlineexamapplication.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class CheckAdminOrUser {

	public static String checkAdminOrUser(HttpServletRequest request, HttpServletResponse response) {

		boolean isAdminCheck = Boolean.valueOf(String.valueOf(request.getSession().getAttribute("isAdmin")));
		 boolean signInCheck=Boolean.valueOf(String.valueOf(request.getSession().getAttribute("signInCheck")));
		 
		 System.out.println(signInCheck+"signin flag valuee");
		 if(signInCheck==false) {
			 System.out.println("==============signin iff");
			 String errMsg="notLogin";
			 String message="To Access This Page You Need To Login First";
			 request.setAttribute("errMsg", errMsg);
			 request.setAttribute("message", message);
			 return OnlineExam.ERROR;
		 }
		if (isAdminCheck == false) {
			String errMsg = "notAdmin";
			String message = "You Are Not Allowed To View This Page";
			request.setAttribute("message", message);
			request.setAttribute("errMsg", errMsg);
			return OnlineExam.ERROR;
		}
		return OnlineExam.SUCCESS;
		
	}
}

package com.vastpro.onlineexamapplication.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.GenericValue;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class CheckLoginFlag {
	static boolean isUserlogin = true;

	public static String afterLogin(HttpServletRequest request, HttpServletResponse response) {
		GenericValue userDetails = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);

		String partyId = OnlineExam.NULL;

		
		if (UtilValidate.isNotEmpty(userDetails)) {
			partyId = (String) userDetails.get("partyId");
			isUserlogin = false;
		}
		request.setAttribute("isUserlogin", isUserlogin);
		request.setAttribute("userFirstName", UserLogin.firstName);
		request.setAttribute("partyId", partyId);
		return OnlineExam.SUCCESS;
	}
}
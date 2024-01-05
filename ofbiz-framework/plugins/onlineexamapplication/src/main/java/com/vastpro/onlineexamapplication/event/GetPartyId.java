package com.vastpro.onlineexamapplication.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ofbiz.base.util.UtilValidate;

import com.ctc.wstx.shaded.msv_core.driver.textui.Debug;
import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class GetPartyId {
	
	public static String getPartyId(HttpServletRequest request,HttpServletResponse response) {
		 HttpSession session = request.getSession();	
		 String errMsg=null;
		String partyId= (String) session.getAttribute("partyId");
		System.out.println(partyId+"party id from sessionnnnn=====================");
		request.setAttribute("partyId", partyId);
		
		if(UtilValidate.isEmpty(partyId)) {
			
			request.setAttribute(errMsg, "unable to get party id from session in backend");
			return OnlineExam.ERROR;
		}
		return OnlineExam.SUCCESS;
		
	}

}

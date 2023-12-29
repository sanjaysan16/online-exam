package org.vastpro.leo.events;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

public class CreateEvents {

	public static final String module=CreateEvents.class.getName();
	
	public static String createEvent(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("=============event called=-============");
		LocalDispatcher dispatcher=(LocalDispatcher) request.getAttribute("dispatcher");
		
		GenericValue userLogin=(GenericValue) request.getSession().getAttribute("userLogin");
		String eid=request.getParameter("eid");
		String name=request.getParameter("name");
		
		
		if(UtilValidate.isEmpty(eid)||UtilValidate.isEmpty(name) ) {
			return "error";
		}
		Map<String, Object>context=UtilMisc.toMap("eid",eid,"name",name,"userLogin",userLogin);
		try {
			dispatcher.runSync("ss", context);
		} catch (GenericServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		return "success";
		
	}
}

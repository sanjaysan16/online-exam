package com.vastpro.onlineexamapplication.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class CheckLoginFlag {
	static boolean isUserlogin = true;

	public static String afterLogin(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator=(Delegator)request.getAttribute(OnlineExam.DELEGATOR);
		GenericValue userDetails = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);

		String partyId = OnlineExam.NULL;

		
		if (UtilValidate.isNotEmpty(userDetails)) {
			partyId = (String) userDetails.get("partyId");
			isUserlogin = false;
			if(UtilValidate.isNotEmpty(partyId)) {
				 
				try {
					GenericValue personDetails = EntityQuery.use(delegator).from("Person").where("partyId",partyId).cache().queryOne();
					if(UtilValidate.isNotEmpty(personDetails)) {
						String firstName=(String)personDetails.get("firstName");
						request.setAttribute("userFirstName", firstName);
						return OnlineExam.SUCCESS;
					}else {
						String errMsg="personDetails are empty in the entity";
						request.setAttribute(" _ERROR_MESSAGE_", errMsg);
						return OnlineExam.ERROR;
					}
				} catch (GenericEntityException e) {
					e.printStackTrace();
				}
				
			}else {
				String errMsg="partyId is empty";
				request.setAttribute(" _ERROR_MESSAGE_", errMsg);
				return OnlineExam.ERROR;
			}
			
		}
		request.setAttribute("isUserlogin", isUserlogin);
		request.setAttribute("partyId", partyId);
		return OnlineExam.SUCCESS;
	}
}
package com.vastpro.onlineexamapplication.event;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class GetUsersOfAdminEvent {

	public static final String module=GetUsersOfAdminEvent.class.getName();
	
	public static String getUsersOfAdmin(HttpServletRequest request,HttpServletResponse response) {
		Delegator delegator=(Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		
		String partyIdOfAdmin=(String) request.getAttribute("partyIdOfAdmin");
		
		List<GenericValue>listOfUsersPartyId=null;
		List<GenericValue>listOfUsers=new ArrayList<GenericValue>();
		List<GenericValue>listOfUserLogin=new ArrayList<GenericValue>();
		Map<String,Object> ListOfUserDetails=new HashMap<>();
		 
		if(UtilValidate.isEmpty(partyIdOfAdmin)||partyIdOfAdmin==null) {
			String errMsg = "partyIdOfAdmin is required";
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}
	
		try {
			Debug.logInfo("=======Getting records from PartyRelationship=======", module);
			listOfUsersPartyId = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdFrom",partyIdOfAdmin).cache().queryList();
			
			if(UtilValidate.isEmpty(listOfUsersPartyId)|| listOfUsersPartyId==null) {
				String errMsg = "admin not founded from PartyRelationship";
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
			for(GenericValue userPartyId:listOfUsersPartyId) {
				String partyIdTo=	(String) userPartyId.get("partyIdTo");
				
				GenericValue user= EntityQuery.use(delegator).from("Person").where("partyId",partyIdTo).cache().queryOne();
				if(UtilValidate.isNotEmpty(user)) {
				GenericValue userLogin= EntityQuery.use(delegator).from("UserLogin").where("partyId",partyIdTo).cache().queryOne();
				   if(UtilValidate.isNotEmpty(userLogin)) {
					    listOfUsers.add(user);
						listOfUserLogin.add(userLogin);
				   }
				}
				
			}
		} catch (Exception e) {
			String errMsg="unable to get records from PartyRelationship "+e.toString();
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}
		if(UtilValidate.isNotEmpty(listOfUsers)&&UtilValidate.isNotEmpty(listOfUserLogin)) {
			ListOfUserDetails.put("ListOfUsers",listOfUsers );
			ListOfUserDetails.put("ListOfUserLoginId",listOfUserLogin );
			request.setAttribute("ListOfUserDetails", ListOfUserDetails);
			return OnlineExam.SUCCESS;	
		}
		
		String errMsg="unable to get records";
		request.setAttribute("ERROR", errMsg);
		return OnlineExam.ERROR;
		
	}
}
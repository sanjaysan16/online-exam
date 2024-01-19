package com.vastpro.onlineexamapplication.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class ResultForAdmin {
	public static String userListForResult(HttpServletRequest request,HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		String examId=request.getParameter("examId");
		String partyId=null;
		Debug.logInfo("this is exam Id", examId);
		List<Map<String,String>> studentListWithPartyId=new ArrayList<Map<String,String>>();
		
		List<Integer> performanceIdOfStudent=new LinkedList<>();
		if(UtilValidate.isNotEmpty(examId)) {
			try {
				List<GenericValue> userExamMapping=EntityQuery.use(delegator).from("UserExamMappingMaster").where("examId",examId).cache().queryList();
				System.out.println(userExamMapping);
				for(GenericValue studentsList:userExamMapping) {
					 partyId=(String)studentsList.get("partyId");
					if(UtilValidate.isNotEmpty(partyId)) {
						GenericValue userLogin=EntityQuery.use(delegator).from("Person").where("partyId",partyId).cache().queryOne();
						if(UtilValidate.isNotEmpty(userLogin)) {
							request.setAttribute("studentUserLogin", userLogin);
							String firstName=(String)userLogin.get("firstName");
							Map<String,String> studentDetails=new HashMap<>();
							studentDetails.put(firstName, partyId);
							studentListWithPartyId.add(studentDetails);
							
						}else {
							String errMsg="no such student was there";
							request.setAttribute("EVENT_MESSAGE", errMsg);
							return OnlineExam.ERROR;
						}
					}else {
						String errMsg="no users are there for this exam";
						request.setAttribute("EVENT_MESSAGE", errMsg);
						return OnlineExam.ERROR;
					}
				}
				List<GenericValue> UserAttemptMaster=EntityQuery.use(delegator).from("UserAttemptMaster").where("partyId",partyId,"examId",examId).cache().queryList();
				for(GenericValue performanceIdValue:UserAttemptMaster) {
					int performanceId=performanceIdValue.getInteger("performanceId");
					performanceIdOfStudent.add(performanceId);
				}
			} catch (GenericEntityException e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("studentListWithPartyId", studentListWithPartyId);
		request.setAttribute("performanceList", performanceIdOfStudent);
		return OnlineExam.SUCCESS;
	}
	
	public static String fullDetailsOfResult(HttpServletRequest request,HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		String partyId=request.getParameter("partyId");
		String examId=request.getParameter("examId");
		if(UtilValidate.isNotEmpty(partyId)&& UtilValidate.isNotEmpty(examId)) {
			try {
				//GenericValue person=EntityQuery.use(delegator).from("Person").where("partyId",partyId).cache().queryOne();
				//String partyId=(String) person.get("partyId");
				
					List<GenericValue> userAttemptMaster=EntityQuery.use(delegator).from("UserAttemptMaster").where("examId",examId,"partyId",partyId).cache().queryList();
					request.setAttribute("userAttemptMaster", userAttemptMaster);
				
			} catch (GenericEntityException e) {
				e.printStackTrace();
			}
		}else {
			String errMsg="partyId or examId is null";
			request.setAttribute("EVENT_MESSAGE", errMsg);
			return OnlineExam.ERROR;
		}
		return OnlineExam.SUCCESS;
	}
	
}

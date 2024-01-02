package com.vastpro.onlineexamapplication.event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class DeleteQuestionEvent {

	public static String deleteQuestion(HttpServletRequest request,HttpServletResponse response) {
		Delegator delegator=(Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher=(LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin=(GenericValue) request.getAttribute(OnlineExam.USERLOGIN);
		
		String result="";
	    Long questionId = Long.parseLong(request.getParameter(OnlineExam.QUESTION_ID));
	    
		  if(!UtilValidate.isEmpty(questionId)) {
			  try {
					dispatcher.runSync("DeleteQuestionService", UtilMisc.toMap(OnlineExam.QUESTION_ID,questionId));
				} catch (GenericServiceException e) {
					 String errMsg = "Unable to create new records in QuestionMaster entity: " + e.toString();
						request.setAttribute("ERROR_MESSAGE", errMsg);
						result=OnlineExam.ERROR;
						request.setAttribute("result", result);
					return OnlineExam.ERROR;
					
				}  
			  result=OnlineExam.SUCCESS;
				request.setAttribute("result", result);
			  return OnlineExam.SUCCESS;
		  }
		  
		String errMsg="question is null";
		request.setAttribute("errMsg", errMsg);
	    return OnlineExam.ERROR;
	}
}

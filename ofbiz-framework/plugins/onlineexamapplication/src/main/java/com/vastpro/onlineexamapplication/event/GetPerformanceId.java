package com.vastpro.onlineexamapplication.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class GetPerformanceId {

	public static String module=UserExamList.class.getName(); 
	
	public static String getPerformanceId(HttpServletRequest request,HttpServletResponse response) {
		Delegator delegator=(Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		GenericValue userLogin=(GenericValue) request.getSession().getAttribute("userLogin");
		String partyId=(String) userLogin.get("partyId");
		String examId=request.getParameter("examId");
		Debug.logInfo("this is the partyId from front end", partyId,module);
		Debug.logInfo("this is the examId from front end", examId,module);
		int performanceId=0;
		if(UtilValidate.isNotEmpty(partyId) && UtilValidate.isNotEmpty(examId)) {
			try {
				Map<String, Object> checkPerformanceId = dispatcher.runSync("getPerformanceId", UtilMisc.toMap("partyId",partyId,"examId",examId));
				if(ServiceUtil.isSuccess(checkPerformanceId)) {
					 performanceId =  (int) checkPerformanceId.get("performanceId");
				}else {
					String errMsg="values are not entered into userAttemptMaster entity";
					request.setAttribute("EVENT_MESSAGE", errMsg);
					return OnlineExam.ERROR;
				}
				
				GenericValue performanceIdFromUserAttemptMaster=EntityQuery.use(delegator).from("UserAttemptMaster").where("performanceId",performanceId).cache().queryOne();
				if(UtilValidate.isNotEmpty(performanceIdFromUserAttemptMaster)) {
					String examIdFromEntity=(String) performanceIdFromUserAttemptMaster.get("examId");
					
					if(UtilValidate.isNotEmpty(examIdFromEntity)) {
						List<GenericValue> topicIdList=EntityQuery.use(delegator).from("ExamTopicMappingMaster").where("examId",examId).cache().queryList();
						for(GenericValue topicsList:topicIdList) {
							String topicId=(String)topicsList.get("topicId");
							Long questionsPerExam=(Long)topicsList.get("questionsPerExam");
							BigDecimal topicPassPercentage=(BigDecimal)topicsList.get("topicPassPercentage");
							dispatcher.runSync("user-attempt-topic-master", UtilMisc.toMap("performanceId",performanceId,"topicId",topicId,"topicPassPercentage",topicPassPercentage,"totalQuestionsInThisTopic",questionsPerExam));
							
						}
						
					}else {
						String errMsg="examId from the ExamTopicMappingMaster is empty";
						request.setAttribute("EVENT_MESSAGE", errMsg);
						return OnlineExam.ERROR;
					}
					
				}else {
					String errMsg="performanceId was not created for attempting the exam";
					request.setAttribute("EVENT_MESSAGE", errMsg);
					return OnlineExam.ERROR;
				}
				
				
				
				
			} catch (GenericServiceException e) {
				e.printStackTrace();
			} catch (GenericEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return OnlineExam.SUCCESS;
	}
	
	
	public static String getQuestionAndAnswer(HttpServletRequest request,HttpServletResponse response) {
		Delegator delegator=(Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		String examId=request.getParameter("examId");
		String partyId=(String)request.getSession().getAttribute("partyId");
		String questionId=(String)request.getAttribute("questionId");
		String submittedAnswer=(String)request.getAttribute("submittedAnswer");
		if(UtilValidate.isNotEmpty(examId) && UtilValidate.isNotEmpty(partyId)) {
			int performanceId=0;
			try {
				GenericValue performanceIdFromEntity=EntityQuery.use(delegator).from("UserAttemptMaster").where("examId",examId,"PartyId",partyId).cache().queryOne();
				if(UtilValidate.isNotEmpty(performanceIdFromEntity)) {
					performanceId=(int) performanceIdFromEntity.get("performanceId");
					
					
					
				}else {
					String errMsg="performanceId was not created for attempting the exam";
					request.setAttribute("EVENT_MESSAGE", errMsg);
					return OnlineExam.ERROR;
				}
			} catch (GenericEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return OnlineExam.SUCCESS;
	}
	
}

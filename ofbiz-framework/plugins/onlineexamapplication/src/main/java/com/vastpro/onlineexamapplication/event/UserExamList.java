package com.vastpro.onlineexamapplication.event;

import java.util.ArrayList;

import java.util.HashMap;
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
import org.apache.ofbiz.service.LocalDispatcher;


import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class UserExamList {
	
	public static String module=UserExamList.class.getName(); 
	
	public static String showExam(HttpServletRequest request,HttpServletResponse response) {
		Delegator delegator=(Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		
		String examId=request.getParameter("examId");
		
		
		Debug.logInfo("examId from front end is", examId,module);
		
		GenericValue userLogin =(GenericValue) request.getSession().getAttribute("userLogin");
		if(UtilValidate.isNotEmpty(userLogin)) {
			String userName = (String)userLogin.get("userLoginId");
			String partyId= (String)userLogin.get("partyId");
			
			request.setAttribute("username", userName);
			request.setAttribute("partyId", partyId);
		}else {
			String errMsg="user details or empty in the userLogin";
			request.setAttribute("EVENT_MESSAGE", errMsg);
			return OnlineExam.ERROR;
		}
		
		
		if(UtilValidate.isNotEmpty(examId)) {
			
			
			List<Map<String,Object>>topicAndQuestionForExam=new ArrayList<Map<String,Object>>();
			
			List<GenericValue>assignedExam=new ArrayList<GenericValue>();
			List<String>topicNameList=new ArrayList<String>();
			try {
				GenericValue durationOfExam=EntityQuery.use(delegator).from("ExamMaster").where("examId",examId).cache().queryOne();
				Long durationMinutes=(Long) durationOfExam.get("durationMinutes");
				String examName=(String) durationOfExam.get("examName");
				request.setAttribute("examName", examName);
				request.setAttribute("durationMinutes",durationMinutes);
				
				assignedExam=EntityQuery.use(delegator).from("ExamTopicMappingMaster").where(OnlineExam.EXAM_ID,examId).cache().queryList();
				
				for(GenericValue topic :assignedExam) {
					String topicId=(String) topic.get("topicId");
					
					GenericValue assignedTopics=EntityQuery.use(delegator).from("TopicMaster").where("topicId",topicId).cache().queryOne();
					String topicName = (String) assignedTopics.get("topicName");
					
					Map<String,Object>topicQuestionMap=new HashMap<String,Object>();
					
					List<GenericValue>listOfQuestionsPerTopic=new ArrayList<GenericValue>();
					listOfQuestionsPerTopic=EntityQuery.use(delegator).from("QuestionMaster").where(OnlineExam.TOPIC_ID,topicId).queryList();
					topicQuestionMap.put(topicName, listOfQuestionsPerTopic);
					topicAndQuestionForExam.add(topicQuestionMap);
					topicNameList.add(topicName);
				}
				request.setAttribute("topicAndQuestionForExam", topicAndQuestionForExam);
				request.setAttribute("topicName", topicNameList);
			} catch (GenericEntityException e) {
				 Debug.logInfo("==============="+e.toString(), module);
				
			}
		}else {
			String errMsg="there is no exam id to find the topics";
			request.setAttribute("EVENT_MESSAGE", errMsg);
			return OnlineExam.ERROR;
			
		}
		
		
	
		
		return OnlineExam.SUCCESS;
		
	}

}
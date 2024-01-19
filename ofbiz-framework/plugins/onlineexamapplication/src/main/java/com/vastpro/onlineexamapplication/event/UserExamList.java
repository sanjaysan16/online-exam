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
		String userName = (String)userLogin.get("userLoginId");
		request.setAttribute("username", userName);
		
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
	
	public static String topicQuestionList(HttpServletRequest request,HttpServletResponse response) {
		Delegator delegator = (Delegator)request.getAttribute("delegator");
		
		String examId=request.getParameter("examId");
		String topicName=request.getParameter("topicName");
		Debug.logInfo("examId", examId,module);
		Debug.logInfo("topicName", topicName,module);
		if(UtilValidate.isNotEmpty(topicName)) {
			try {
				GenericValue topicIdFromTopicMaster=EntityQuery.use(delegator).from("TopicMaster").where("topicName",topicName).cache().queryOne();
				if(UtilValidate.isNotEmpty(topicIdFromTopicMaster)) {
					String topicId=(String)topicIdFromTopicMaster.get("topicId");
					Debug.logInfo("topicId", topicId,module);
					if(UtilValidate.isNotEmpty(topicId)) {
						List<GenericValue> questionsFromQuestionMaster=EntityQuery.use(delegator).from("QuestionMaster").where("topicId",topicId).cache().queryList();
						request.setAttribute("questionsFromQuestionMaster", questionsFromQuestionMaster);
						System.out.println("generic value of the questions"+questionsFromQuestionMaster);
					}
					else {
						String errMsg="topic id from the topicMaster entity is empty";
						request.setAttribute("EVENT_MESSAGE", errMsg);
						return OnlineExam.ERROR;
					}
					
				}else {
					String errMsg="topic is not there in the topicMaster entity";
					request.setAttribute("EVENT_MESSAGE", errMsg);
					return OnlineExam.ERROR;
				}
				
			} catch (GenericEntityException e) {
				e.printStackTrace();
			}
		}else {
			String errMsg="topic name from frontend is empty";
			request.setAttribute("EVENT_MESSAGE", errMsg);
			return OnlineExam.ERROR;
		}
		
		return OnlineExam.SUCCESS;
	}

}
package com.vastpro.onlineexamapplication.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

public class ViewQuestionsService {
	
	
	public static Map<String,Object> viewQuestionsService(DispatchContext dtx,Map<String,? extends Object> context){
		System.out.println("=============iam inside the ViewQuestionService=============");
		Delegator delegator=dtx.getDelegator();
		
		Map<String,Object>result=ServiceUtil.returnSuccess();
		
		List<Map<String,Object>>listOfQuestion= new LinkedList<>();
			List<GenericValue>questionList=null;
		try {
			questionList=EntityQuery.use(delegator).from("QuestionMaster").queryList();
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(GenericValue questionGenericValue:questionList) {
			Map<String,Object> question=new HashMap<>();
			String questionDetail=questionGenericValue.getString("questionDetail");
			String questionId=questionGenericValue.getString("questionId");
			String optionA=questionGenericValue.getString("optionA");
			String optionB=questionGenericValue.getString("optionB");
			String optionC=questionGenericValue.getString("optionC");
			String optionD=questionGenericValue.getString("optionD");
			String optionE=questionGenericValue.getString("optionE");
			String answer=questionGenericValue.getString("answer");
			String numAnswers=questionGenericValue.getString("numAnswers");
			String questionType=questionGenericValue.getString("questionType");
			String difficultyLeavel=questionGenericValue.getString("difficultyLevel");
			String answerValue=questionGenericValue.getString("answerValue");
			String topicId=questionGenericValue.getString("topicId");
			String negativeMarkValue=questionGenericValue.getString("negativeMarkValue");
			
			question.put(questionDetail, "questionDetail");
			question.put(questionId, "questionId");
			question.put(optionA, "optionA");
			question.put(optionB, "optionB");
		   question.put(optionC, "optionC");
			question.put(optionD, "optionD");
			question.put(optionE, "optionE");
			question.put(answer, "answer");
			question.put(numAnswers, "numAnswer");
			question.put(questionType, "questionType");
			question.put(difficultyLeavel, "difficultyLeavel");
			question.put(answerValue, "answerValue");
			question.put(topicId, "topicId");
			question.put(negativeMarkValue, "negativeMarkValue");
      System.out.println("after the query======================");
      
      

			listOfQuestion.add(questionGenericValue);
			



			
			
		}
		
		
		 result.put("listOfQuestion", listOfQuestion) ;
		 
		 
		
		return result;
		
	}
	


}

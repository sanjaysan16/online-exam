package com.vastpro.onlineexamapplication.event;

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

import com.vastpro.onlineexamapplication.constant.OnlineExam;



public class UpdateQuestionMaster {
	public static final String module = UpdateQuestionMaster.class.getName();
    
	public static String updateQuestionMaster(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin = (GenericValue)request.getSession().getAttribute(OnlineExam.USERLOGIN);

		GenericValue questionNullCheck=null;
		
		String result="";
		
		Long questionId = Long.parseLong((String) request.getParameter(OnlineExam.QUESTION_ID));
		
		
		if(UtilValidate.isNotEmpty(questionId)||questionNullCheck!=null) {
			try {
				questionNullCheck=EntityQuery.use(delegator).from(OnlineExam.Question_Master).where(OnlineExam.QUESTION_ID,questionId).cache().queryFirst();
				request.setAttribute("questionNullCheck", questionNullCheck);
				
			} catch (GenericEntityException e1) {
				String errMsg="error while querying entity: =   "+e1.toString();
				request.setAttribute("ERROR_MESSAGE", errMsg);
			}
			
			
			String questionDetail = (String) request.getAttribute(OnlineExam.QUESTION_DETAIL);
			
			if (UtilValidate.isEmpty(questionDetail)) {
				String errMsg = "questionDetail is empty";
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return OnlineExam.ERROR;
			}
			
			String optionA = (String) request.getAttribute(OnlineExam.OPTION_A);
			String optionB = (String) request.getAttribute(OnlineExam.OPTION_B);
			String optionC = (String) request.getAttribute(OnlineExam.OPTION_C);
			String optionD = (String) request.getAttribute(OnlineExam.OPTION_D);
			String optionE = (String) request.getAttribute(OnlineExam.OPTION_E);
			String answer = (String) request.getAttribute(OnlineExam.ANSWER);
			String numAnswers= (String) request.getAttribute(OnlineExam.NUM_ANSWERS);
			String questionType = (String) request.getAttribute(OnlineExam.QUESTION_TYPE);
			String difficultyLevel = (String) request.getAttribute(OnlineExam.DIFFICULTY_LEVEL);
			String answerValue = (String) request.getAttribute(OnlineExam.ANSWER_VALUE);
			String topicId = (String) request.getParameter(OnlineExam.TOPIC_ID);
			String negativeMarkValue = (String) request.getAttribute(OnlineExam.NEGATIVE_MARK_VALUE);

			try {
				
				dispatcher.runSync("updateQuestionMaster",
						UtilMisc.toMap(OnlineExam.QUESTION_ID,questionId,OnlineExam.QUESTION_DETAIL, questionDetail, OnlineExam.OPTION_A, optionA, OnlineExam.OPTION_B,
								optionB, OnlineExam.OPTION_C, optionC, OnlineExam.OPTION_D, optionD,
								OnlineExam.OPTION_E, optionE, OnlineExam.ANSWER, answer, OnlineExam.NUM_ANSWERS,
								numAnswers,OnlineExam.QUESTION_TYPE, questionType, OnlineExam.DIFFICULTY_LEVEL, difficultyLevel,
								OnlineExam.ANSWER_VALUE, answerValue,OnlineExam.TOPIC_ID,topicId,OnlineExam.NEGATIVE_MARK_VALUE,negativeMarkValue,OnlineExam.USERLOGIN,userLogin));
			} catch (GenericServiceException e) {
				 String errMsg = "Unable to update records in QuestionMaster entity: " + e.toString();
					request.setAttribute("ERROR_MESSAGE", errMsg);
				return OnlineExam.ERROR;
			}
			
			result=OnlineExam.SUCCESS;
	        request.setAttribute("result",result);
			return OnlineExam.SUCCESS;

			
		}
		
			
			result="Question Not present in This Database";
			request.setAttribute("result", result);
			
			return result;
		

	}
}
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



public class CreateQuestionMaster {
	public static final String module = CreateQuestionMaster.class.getName();

	public static String createQuestionMaster(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);

		GenericValue questionDetailCheck;

		String result = "";

		String topicId = request.getParameter(OnlineExam.TOPIC_ID);
		String questionDetail = (String) request.getAttribute(OnlineExam.QUESTION_DETAIL);

		try {
			questionDetailCheck = EntityQuery.use(delegator).from(OnlineExam.Question_Master)
					.where(OnlineExam.QUESTION_DETAIL, questionDetail).cache().queryOne();

			if (UtilValidate.isEmpty(questionDetailCheck)) {

				String optionA = (String) request.getAttribute(OnlineExam.OPTION_A);
				String optionB = (String) request.getAttribute(OnlineExam.OPTION_B);
				String optionC = (String) request.getAttribute(OnlineExam.OPTION_C);
				String optionD = (String) request.getAttribute(OnlineExam.OPTION_D);
				String optionE = (String) request.getAttribute(OnlineExam.OPTION_E);
				String answer = (String) request.getAttribute(OnlineExam.ANSWER);
				String numAnswers = (String) request.getAttribute(OnlineExam.NUM_ANSWERS);
				String questionType = (String) request.getAttribute(OnlineExam.QUESTION_TYPE);
				String difficultyLevel = (String) request.getAttribute(OnlineExam.DIFFICULTY_LEVEL);
				String answerValue = (String) request.getAttribute(OnlineExam.ANSWER_VALUE);

				String negativeMarkValue = (String) request.getAttribute(OnlineExam.NEGATIVE_MARK_VALUE);

				try {
					
					dispatcher.runSync("createQuestionMaster",
							UtilMisc.toMap(OnlineExam.QUESTION_DETAIL, questionDetail,OnlineExam.OPTION_A, optionA, OnlineExam.OPTION_B, optionB,
									OnlineExam.OPTION_C, optionC,OnlineExam.OPTION_D, optionD, OnlineExam.OPTION_E, optionE, OnlineExam.ANSWER, answer,
									OnlineExam.NUM_ANSWERS, numAnswers, OnlineExam.QUESTION_TYPE, questionType, OnlineExam.DIFFICULTY_LEVEL,
									difficultyLevel, "answerValue", answerValue, "topicId", topicId,
									OnlineExam.NEGATIVE_MARK_VALUE, negativeMarkValue, OnlineExam.USERLOGIN, userLogin));
				} catch (GenericServiceException e) {
					String errMsg = "Unable to create new records in QuestionMaster entity: " + e.toString();
					result = OnlineExam.ERROR;
					request.setAttribute("result", result);
					request.setAttribute("ERROR_MESSAGE", errMsg);
					return OnlineExam.ERROR;
				}
				result = OnlineExam.SUCCESS;
				request.setAttribute("result", result);
				request.setAttribute("EVENT_MESSAGE", "QuestionMaster created succesfully.");
				return OnlineExam.SUCCESS;

			} else {
				result = OnlineExam.ERROR;
				String errMsg = "Question Is Alredy Exist";
				request.setAttribute("errMsg", errMsg);
				request.setAttribute("result", result);
			}

		} catch (GenericEntityException e) {
			String errMsg = "unable to get questionDetail from entity QuestionMsaster" + e.toString();
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}
		return OnlineExam.ERROR;

	}
}

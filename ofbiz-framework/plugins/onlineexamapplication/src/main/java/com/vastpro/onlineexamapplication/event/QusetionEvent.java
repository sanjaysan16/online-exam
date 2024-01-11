package com.vastpro.onlineexamapplication.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.product.product.ProductUtilServices;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class QusetionEvent {

	// create New Question method

	public static String createNewQuestion(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);

		GenericValue questionDetailCheck;

		String result = "";

		Map<String, Object> fields = UtilHttp.getCombinedMap(request);

		String topicId = (String) fields.get(OnlineExam.TOPIC_ID);
		String questionDetail = (String) fields.get(OnlineExam.QUESTION_DETAIL);

		try {
			
			
			questionDetailCheck = EntityQuery.use(delegator).from(OnlineExam.Question_Master)
					.where(OnlineExam.QUESTION_DETAIL, questionDetail).cache().queryOne();

			if (UtilValidate.isEmpty(questionDetailCheck)) {

				String optionA = (String) fields.get(OnlineExam.OPTION_A);
				String optionB = (String) fields.get(OnlineExam.OPTION_B);
				String optionC = (String) fields.get(OnlineExam.OPTION_C);
				String optionD = (String) fields.get(OnlineExam.OPTION_D);
				String optionE = (String) fields.get(OnlineExam.OPTION_E);
				String answer = (String) fields.get(OnlineExam.ANSWER);
				String numAnswers = (String) fields.get(OnlineExam.NUM_ANSWERS);
				String questionType = (String) fields.get(OnlineExam.QUESTION_TYPE);
				String difficultyLevel = (String) fields.get(OnlineExam.DIFFICULTY_LEVEL);
				String answerValue = (String) fields.get(OnlineExam.ANSWER_VALUE);

				String negativeMarkValue = (String) fields.get(OnlineExam.NEGATIVE_MARK_VALUE);

				// field validation
				if (UtilValidate.isNotEmpty(topicId) || UtilValidate.isNotEmpty(optionA)
						|| UtilValidate.isNotEmpty(optionB) || UtilValidate.isNotEmpty(optionC)
						|| UtilValidate.isNotEmpty(optionD) || UtilValidate.isNotEmpty(optionE)
						|| UtilValidate.isNotEmpty(answer) || UtilValidate.isNotEmpty(numAnswers)
						|| UtilValidate.isNotEmpty(questionType)) {
					Map<String, Object> result2 = null;

					try {

						result2 = dispatcher.runSync("createNewQuestionService",
								UtilMisc.toMap(OnlineExam.QUESTION_DETAIL, questionDetail, OnlineExam.OPTION_A, optionA,
										OnlineExam.OPTION_B, optionB, OnlineExam.OPTION_C, optionC, OnlineExam.OPTION_D,
										optionD, OnlineExam.OPTION_E, optionE, OnlineExam.ANSWER, answer,
										OnlineExam.NUM_ANSWERS, numAnswers, OnlineExam.QUESTION_TYPE, questionType,
										OnlineExam.DIFFICULTY_LEVEL, difficultyLevel, "answerValue", answerValue,
										"topicId", topicId, OnlineExam.NEGATIVE_MARK_VALUE, negativeMarkValue,
										OnlineExam.USERLOGIN, userLogin));
					} catch (GenericServiceException e) {
						String errMsg = "Unable to create new records in QuestionMaster entity: " + e.toString();
						result = OnlineExam.ERROR;
						request.setAttribute("result", result);
						request.setAttribute("ERROR_MESSAGE", errMsg);
						return OnlineExam.ERROR;
					}
					if (ServiceUtil.isSuccess(result2)) {
						result = OnlineExam.SUCCESS;
						request.setAttribute("result", result);
						request.setAttribute("result2", result2);
						request.setAttribute("EVENT_MESSAGE", "QuestionMaster created succesfully.");
						return OnlineExam.SUCCESS;
					}

				} else {
					result = OnlineExam.ERROR;
					String errMsg = "please enter the required fields";
					request.setAttribute("errMsg", errMsg);
					request.setAttribute("result", result);
				}

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

	// Update Question Method......

	public static String updateExistingQuestion(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);

		GenericValue questionNullCheck = null;

		String result = "";
		Map<String, Object> fields = UtilHttp.getCombinedMap(request);

		Long questionId = Long.parseLong((String) fields.get(OnlineExam.QUESTION_ID));

		if (UtilValidate.isNotEmpty(questionId)) {
			try {
				questionNullCheck = EntityQuery.use(delegator).from(OnlineExam.Question_Master)
						.where(OnlineExam.QUESTION_ID, questionId).cache().queryFirst();
				request.setAttribute("questionNullCheck", questionNullCheck);

			} catch (GenericEntityException e1) {
				String errMsg = "error while querying entity: =   " + e1.toString();
				request.setAttribute("ERROR_MESSAGE", errMsg);
			}

			String questionDetail = (String) fields.get(OnlineExam.QUESTION_DETAIL);

			if (UtilValidate.isEmpty(questionDetail)) {
				String errMsg = "questionDetail is empty";
				request.setAttribute("ERROR_MESSAGE", errMsg);
				return OnlineExam.ERROR;
			}

			String optionA = (String) fields.get(OnlineExam.OPTION_A);
			String optionB = (String) fields.get(OnlineExam.OPTION_B);
			String optionC = (String) fields.get(OnlineExam.OPTION_C);
			String optionD = (String) fields.get(OnlineExam.OPTION_D);
			String optionE = (String) fields.get(OnlineExam.OPTION_E);
			String answer = (String) fields.get(OnlineExam.ANSWER);
			String numAnswers = (String) fields.get(OnlineExam.NUM_ANSWERS);
			String questionType = (String) fields.get(OnlineExam.QUESTION_TYPE);
			String difficultyLevel = (String) fields.get(OnlineExam.DIFFICULTY_LEVEL);
			String answerValue = (String) fields.get(OnlineExam.ANSWER_VALUE);
			String topicId = (String) fields.get(OnlineExam.TOPIC_ID);
			String negativeMarkValue = (String) fields.get(OnlineExam.NEGATIVE_MARK_VALUE);

			if (UtilValidate.isNotEmpty(topicId) || UtilValidate.isNotEmpty(optionA) || UtilValidate.isNotEmpty(optionB)
					|| UtilValidate.isNotEmpty(optionC) || UtilValidate.isNotEmpty(optionD)
					|| UtilValidate.isNotEmpty(optionE) || UtilValidate.isNotEmpty(answer)
					|| UtilValidate.isNotEmpty(numAnswers) || UtilValidate.isNotEmpty(questionType)) {
				Map<String, Object> result2 = null;
				try {

					result2 = dispatcher.runSync("updateExistingQuestionService", UtilMisc.toMap(OnlineExam.QUESTION_ID,
							questionId, OnlineExam.QUESTION_DETAIL, questionDetail, OnlineExam.OPTION_A, optionA,
							OnlineExam.OPTION_B, optionB, OnlineExam.OPTION_C, optionC, OnlineExam.OPTION_D, optionD,
							OnlineExam.OPTION_E, optionE, OnlineExam.ANSWER, answer, OnlineExam.NUM_ANSWERS, numAnswers,
							OnlineExam.QUESTION_TYPE, questionType, OnlineExam.DIFFICULTY_LEVEL, difficultyLevel,
							OnlineExam.ANSWER_VALUE, answerValue, OnlineExam.TOPIC_ID, topicId,
							OnlineExam.NEGATIVE_MARK_VALUE, negativeMarkValue, OnlineExam.USERLOGIN, userLogin));
				} catch (GenericServiceException e) {
					String errMsg = "Unable to update records in QuestionMaster entity: " + e.toString();
					request.setAttribute("ERROR_MESSAGE", errMsg);
					return OnlineExam.ERROR;
				}
				if (ServiceUtil.isSuccess(result2)) {
					result = OnlineExam.SUCCESS;
					request.setAttribute("result", result);
					request.setAttribute("result2", result2);
					return OnlineExam.SUCCESS;
				} else {
					result = OnlineExam.ERROR;
					request.setAttribute("result", result);
					request.setAttribute("result2", result2);
				}

			} else {
				request.setAttribute("errMsg", "please enter all the required fields");
			}

		}

		result = "Question Not present in This Database";
		request.setAttribute("result", result);

		return result;

	}

	// view list of question method.......

	public static String viewQuestions(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);

		String topicId = request.getParameter(OnlineExam.TOPIC_ID);

		if (UtilValidate.isNotEmpty(topicId)) {

			List<GenericValue> questionList = new ArrayList<GenericValue>();

			try {
				questionList = EntityQuery.use(delegator).from(OnlineExam.Question_Master)
						.where(OnlineExam.TOPIC_ID, topicId).cache().queryList();
			} catch (GenericEntityException e) {
				String err = "unable to connect QuestionMaster entity" + e.toString();
				request.setAttribute("error", err);
				return OnlineExam.ERROR;
			}

			if (UtilValidate.isEmpty(questionList) || questionList == null) {
				String err = "There is no  Question In This topic";
				request.setAttribute("error", err);

				return OnlineExam.ERROR;
			}
			request.setAttribute("questionList", questionList);

			return OnlineExam.SUCCESS;

		}

		String errMsg = "TopicId iss Null or empty";
		request.setAttribute("errMsg", errMsg);
		return OnlineExam.ERROR;

	}

	// Delete Question Method.....

	public static String deleteQuestion(HttpServletRequest request, HttpServletResponse response) {

		
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getAttribute(OnlineExam.USERLOGIN);

		String result = "";
		Long questionId = Long.parseLong(request.getParameter(OnlineExam.QUESTION_ID));

		if (!UtilValidate.isEmpty(questionId)) {
			try {
				dispatcher.runSync("deleteQuestionService", UtilMisc.toMap(OnlineExam.QUESTION_ID, questionId));
			} catch (GenericServiceException e) {
				String errMsg = "Unable to create new records in QuestionMaster entity: " + e.toString();
				request.setAttribute("ERROR_MESSAGE", errMsg);
				result = OnlineExam.ERROR;
				request.setAttribute("result", result);
				return OnlineExam.ERROR;

			}
			result = OnlineExam.SUCCESS;
			request.setAttribute("result", result);
			return OnlineExam.SUCCESS;
		}

		String errMsg = "question is null";
		request.setAttribute("errMsg", errMsg);
		return OnlineExam.ERROR;
	}

}

package com.vastpro.onlineexamapplication.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

public class ExamMasterEvent {
	public static final String module = ExamMasterEvent.class.getName();

	/**
	 * addExamEvent
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String createExam(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);

		String result = "";

		GenericValue examCheck;

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String examName = (String) request.getAttribute(OnlineExam.EXAM_NAME);

		// CHECKING EXAM IS THERE OR NOT
		try {
			examCheck = EntityQuery.use(delegator).from(OnlineExam.Exam_Master).where(OnlineExam.EXAM_NAME, examName)
					.cache().queryFirst();

			if (UtilValidate.isEmpty(examCheck)) {

				String description = (String) request.getAttribute(OnlineExam.DESCRIPTION);

				String creationDateString = (String) request.getAttribute(OnlineExam.CREATION_DATE);
				LocalDateTime creationLocalDateTime = LocalDateTime.parse(creationDateString, inputFormatter);
				String creationDate = creationLocalDateTime.format(outputFormatter);

				String expirationDateString = (String) request.getAttribute(OnlineExam.EXPIRATION_DATE);
				LocalDateTime expirationLocalDateTime = LocalDateTime.parse(expirationDateString, inputFormatter);
				String expirationDate = expirationLocalDateTime.format(outputFormatter);

				String noOfQuestions = (String) request.getAttribute(OnlineExam.NO_OF_QUESTIONS);
				String durationMinutes = (String) request.getAttribute(OnlineExam.DURATION_MINUTES);
				String passPercentage = (String) request.getAttribute(OnlineExam.PASS_PERCENTAGE);
				String questionsRandomized = (String) request.getAttribute(OnlineExam.QUESTIONS_RANDOMIZED);
				String answersMust = (String) request.getAttribute(OnlineExam.ANSWERS_MUST);
				String enableNegativeMark = (String) request.getAttribute(OnlineExam.ENABLE_NEGATIVE_MARK);
				String negativeMarkValue = (String) request.getAttribute(OnlineExam.NEGATIVE_MARK_VALUE);

				// CALLING SERVICE TO CREATE EXAM
				try {
					dispatcher.runSync("createExamService",
							UtilMisc.toMap(OnlineExam.EXAM_NAME, examName, OnlineExam.DESCRIPTION, description,
									OnlineExam.CREATION_DATE, creationDate, OnlineExam.EXPIRATION_DATE, expirationDate,
									OnlineExam.NO_OF_QUESTIONS, noOfQuestions, OnlineExam.DURATION_MINUTES,
									durationMinutes, OnlineExam.PASS_PERCENTAGE, passPercentage,
									OnlineExam.QUESTIONS_RANDOMIZED, questionsRandomized, OnlineExam.ANSWERS_MUST,
									answersMust, OnlineExam.ENABLE_NEGATIVE_MARK, enableNegativeMark,
									OnlineExam.NEGATIVE_MARK_VALUE, negativeMarkValue));
				} catch (GenericServiceException e) {
					String errMsg = "Unable to create new records in OfbizDemo entity: " + e.toString();
					request.setAttribute("ERROR_MESSAGE", errMsg);
					return OnlineExam.ERROR;
				}

				request.setAttribute("EVENT_MESSAGE", "OFBiz Demo created succesfully.");
				result = OnlineExam.SUCCESS;
				request.setAttribute("result", result);
				return OnlineExam.SUCCESS;
			} else {
				result = OnlineExam.ERROR;
				request.setAttribute("result", result);
				String errMsg = OnlineExam.EXAMISALREDYEXIST;
				request.setAttribute("errMsg", errMsg);

			}

		} catch (GenericEntityException e1) {

			e1.printStackTrace();
		}

		return OnlineExam.SUCCESS;

	}

	/**
	 * updateExam
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String updateExam(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String examId = (String) request.getAttribute(OnlineExam.EXAM_ID);
		String examName = (String) request.getAttribute(OnlineExam.EXAM_NAME);

		if (UtilValidate.isEmpty(examName) || UtilValidate.isEmpty(examId)) {
			String errMsg = "examId examName is required fields";
			request.setAttribute("ERROR_MESSAGE", errMsg);
			return OnlineExam.ERROR;
		}
		String description = (String) request.getAttribute(OnlineExam.DESCRIPTION);
		String creationDateString = (String) request.getAttribute("creationDate");

		// creationDateString converting to localtime
		LocalDateTime creationLocalDateTime = LocalDateTime.parse(creationDateString, inputFormatter);
		String creationDate = creationLocalDateTime.format(outputFormatter);

		String expirationDateString = (String) request.getAttribute("expirationDate");
		// expirationDateString converting to localtime
		LocalDateTime expirationLocalDateTime = LocalDateTime.parse(expirationDateString, inputFormatter);
		String expirationDate = expirationLocalDateTime.format(outputFormatter);

		String noOfQuestions = (String) request.getAttribute("noOfQuestions");
		String durationMinutes = (String) request.getAttribute("durationMinutes");
		String passPercentage = (String) request.getAttribute("passPercentage");
		String questionsRandomized = (String) request.getAttribute("questionsRandomized");
		String answersMust = (String) request.getAttribute("answersMust");
		String enableNegativeMark = (String) request.getAttribute("enableNegativeMark");
		String negativeMarkValue = (String) request.getAttribute("negativeMarkValue");
		System.out.println(".............................");
		System.out.println(".............................");
		System.out.println(".............................");
		System.out.println(enableNegativeMark);
		System.out.println(negativeMarkValue);

		try {
			Debug.logInfo("=======Updating Exam record in event using service updateExam=========", module);
			dispatcher.runSync("updateExam",
					UtilMisc.toMap("examId", examId, "examName", examName, "description", description, "creationDate",
							creationDate, "expirationDate", expirationDate, "noOfQuestions", noOfQuestions,
							"durationMinutes", durationMinutes, "passPercentage", passPercentage, "questionsRandomized",
							questionsRandomized, "answersMust", answersMust, "enableNegativeMark", enableNegativeMark,
							"negativeMarkValue", negativeMarkValue, "userLogin", userLogin));
		} catch (GenericServiceException e) {
			String errMsg = "Unable to update records in Exam entity: " + e.toString();
			request.setAttribute("ERROR_MESSAGE", errMsg);
			return OnlineExam.ERROR;
		}

		request.setAttribute("EVENT_MESSAGE", "Exam updated succesfully.");
		return OnlineExam.SUCCESS;

	}

	/**
	 * deleteExam
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String deleteExam(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);

		String examId = (String) request.getAttribute(OnlineExam.EXAM_ID);

		if (UtilValidate.isEmpty(examId)) {
			String errMsg = "could not find examId as " + examId;
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}

		try {
			try {

				GenericValue examGv = EntityQuery.use(delegator).from(OnlineExam.Exam_Master)
						.where(OnlineExam.EXAM_ID, examId).cache().queryOne();

				if (UtilValidate.isNotEmpty(examId)) {
					List<GenericValue> examTopicMappingListGv = EntityQuery.use(delegator)
							.from(OnlineExam.EXAM_TOPIC_MAPPING_MASTER).where(OnlineExam.EXAM_ID, examId).cache()
							.queryList();

					if (UtilValidate.isNotEmpty(examTopicMappingListGv)) {
						for (GenericValue examTopicMapping : examTopicMappingListGv) {
							String topicId = (String) examTopicMapping.get(OnlineExam.TOPIC_ID);
							List<GenericValue> ListOfQuestions = EntityQuery.use(delegator)
									.from(OnlineExam.Question_Master).where(OnlineExam.TOPIC_ID, topicId).cache()
									.queryList();

							if (UtilValidate.isNotEmpty(ListOfQuestions)) {

								for (GenericValue question : ListOfQuestions) {
									Long questionId = (Long) question.get(OnlineExam.QUESTION_ID);
									dispatcher.runSync("DeleteQuestionService", UtilMisc.toMap(OnlineExam.QUESTION_ID,
											questionId, OnlineExam.USERLOGIN, userLogin));
								}
							}
							Map<String, Object> deleteExamTopicMapping = dispatcher.runSync("deleteExamTopicMapping",
									UtilMisc.toMap(OnlineExam.EXAM_ID, examId, OnlineExam.TOPIC_ID, topicId,
											OnlineExam.USERLOGIN, userLogin));

							if (ServiceUtil.isSuccess(deleteExamTopicMapping)) {
								Map<String, Object> deleteTopicMaster = dispatcher.runSync("deleteTopicMaster",
										UtilMisc.toMap(OnlineExam.TOPIC_ID, topicId, OnlineExam.USERLOGIN, userLogin));
							} else {
								request.setAttribute("service_error", "service(deleteTopicMaster) failed");
								return OnlineExam.ERROR;
							}
						}
					}
					dispatcher.runSync("deleteExam",
							UtilMisc.toMap(OnlineExam.EXAM_ID, examId, OnlineExam.USERLOGIN, userLogin));
					request.setAttribute("_EVENT_MESSAGE", "Exam deleted succesfully.");
					return OnlineExam.SUCCESS;
				} else {
					String errMsg = "unable to find exam from Exam entity";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
			} catch (GenericEntityException e) {
				String errMsg = "unable to get record from Exam entity" + e.toString();
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
		} catch (GenericServiceException e) {
			String errMsg = "unable to delete record from Exam entity" + e.toString();
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}

	}

	/**
	 * getExamOrExamList
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 */
	public static String getExamOrExamList(HttpServletRequest request, HttpServletResponse reponse) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		List<GenericValue> examList = null;
		GenericValue exam = null;
		String examId = (String) request.getAttribute("examId");
		if (examId != null) {
			try {
				Debug.logInfo("==========Geting exam from Exam entity==========", module);
				exam = EntityQuery.use(delegator).from("ExamMaster").where("examId", examId).cache().queryOne();
				if (exam == null) {
					String errMsg = "no exam founded from entity Exam";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
			} catch (GenericEntityException e) {
				String errMsg = "unable to get record from entity Exam" + e.toString();
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
			request.setAttribute("getExam", exam);
			return OnlineExam.SUCCESS;
		} else {
			try {
				Debug.logInfo("==========Geting examList from Exam entity==========", module);
				examList = EntityQuery.use(delegator).from("ExamMaster").cache().queryList();
			} catch (GenericEntityException e) {
				String errMsg = "unable to get records from entity Exam" + e.toString();
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
			request.setAttribute("Exam_List", examList);
			return OnlineExam.SUCCESS;
		}
	}
}

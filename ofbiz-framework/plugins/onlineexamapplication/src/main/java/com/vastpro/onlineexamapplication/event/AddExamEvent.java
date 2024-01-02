package com.vastpro.onlineexamapplication.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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



public class AddExamEvent {

	public static String addExamEvent(HttpServletRequest request, HttpServletResponse response) {
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
			examCheck = EntityQuery.use(delegator).from(OnlineExam.Exam_Master).where(OnlineExam.EXAM_NAME, examName).cache().queryFirst();
            
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

				//CALLING SERVICE TO CREATE EXAM
				try {
					dispatcher.runSync("createAddExamService",
							UtilMisc.toMap(OnlineExam.EXAM_NAME, examName, OnlineExam.DESCRIPTION, description, OnlineExam.CREATION_DATE,
									creationDate, OnlineExam.EXPIRATION_DATE, expirationDate, OnlineExam.NO_OF_QUESTIONS, noOfQuestions,
									OnlineExam.DURATION_MINUTES, durationMinutes,OnlineExam.PASS_PERCENTAGE, passPercentage,
									OnlineExam.QUESTIONS_RANDOMIZED, questionsRandomized,OnlineExam.ANSWERS_MUST, answersMust,
									OnlineExam.ENABLE_NEGATIVE_MARK, enableNegativeMark,OnlineExam.NEGATIVE_MARK_VALUE, negativeMarkValue));
				} catch (GenericServiceException e) {
					String errMsg = "Unable to create new records in OfbizDemo entity: " + e.toString();
					request.setAttribute("_ERROR_MESSAGE_", errMsg);
					return OnlineExam.ERROR;
				}

				request.setAttribute("_EVENT_MESSAGE_", "OFBiz Demo created succesfully.");
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

}

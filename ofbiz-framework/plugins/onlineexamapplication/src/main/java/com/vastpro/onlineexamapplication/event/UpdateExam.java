package com.vastpro.onlineexamapplication.event;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import com.vastpro.onlineexamapplication.constant.OnlineExam;



public class UpdateExam {
	public static final String module = UpdateExam.class.getName();

	public static String updateExam(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 
		String examId=(String) request.getAttribute(OnlineExam.EXAM_ID);
		String examName = (String) request.getAttribute(OnlineExam.EXAM_NAME);

		if (UtilValidate.isEmpty(examName)||UtilValidate.isEmpty(examId)) {
			String errMsg = "examId examName is required fields";
			request.setAttribute("ERROR_MESSAGE", errMsg);
			return OnlineExam.ERROR;
		}
		String description = (String) request.getAttribute(OnlineExam.DESCRIPTION);
        String creationDateString=(String) request.getAttribute("creationDate");
        
		//creationDateString converting to localtime
		LocalDateTime creationLocalDateTime=LocalDateTime.parse(creationDateString, inputFormatter);
		String creationDate=creationLocalDateTime.format(outputFormatter);
		
		String expirationDateString=(String)request.getAttribute("expirationDate");
		//expirationDateString converting to localtime
		LocalDateTime expirationLocalDateTime=LocalDateTime.parse(expirationDateString, inputFormatter);
		String expirationDate=expirationLocalDateTime.format(outputFormatter);
		
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
			Debug.logInfo(
					"=======Updating Exam record in event using service updateExam=========",
					module);
			dispatcher.runSync("updateExam",
					UtilMisc.toMap("examId",examId,"examName", examName, "description", description, "creationDate",
							creationDate, "expirationDate", expirationDate, "noOfQuestions", noOfQuestions,
							"durationMinutes", durationMinutes, "passPercentage", passPercentage, "questionsRandomized",
							questionsRandomized, "answersMust", answersMust, "enableNegativeMark", enableNegativeMark,
							"negativeMarkValue", negativeMarkValue,"userLogin",userLogin));
		} catch (GenericServiceException e) {	
			 String errMsg = "Unable to update records in Exam entity: " + e.toString();
				request.setAttribute("ERROR_MESSAGE", errMsg);
			return OnlineExam.ERROR;
		}

        request.setAttribute("EVENT_MESSAGE", "Exam updated succesfully.");
		return OnlineExam.SUCCESS;

	}
}
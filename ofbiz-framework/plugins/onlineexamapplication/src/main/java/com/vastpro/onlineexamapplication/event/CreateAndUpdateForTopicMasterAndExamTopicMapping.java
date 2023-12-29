package com.vastpro.onlineexamapplication.event;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class CreateAndUpdateForTopicMasterAndExamTopicMapping {
	public static final String module = CreateAndUpdateForTopicMasterAndExamTopicMapping.class.getName();

	public static String createTopicMaster(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);

		String examId = (String) request.getAttribute(OnlineExam.EXAM_ID);
		String topicIdFromFrontEnd = (String) request.getAttribute(OnlineExam.TOPIC_ID);

		String topicName = (String) request.getAttribute(OnlineExam.TOPIC_NAME);
		String percentage = (String) request.getAttribute(OnlineExam.PERCENTAGE);
		String passPercentage = (String) request.getAttribute(OnlineExam.TOPIC_PASS_PERCENTAGE);

		if (topicIdFromFrontEnd != "") {
			if (examId != OnlineExam.NULL) {
				try {
					
					Map<String, Object> editTopicMaster = dispatcher.runSync("editTopicMaster",
							UtilMisc.toMap(OnlineExam.TOPIC_ID, topicIdFromFrontEnd, OnlineExam.TOPIC_NAME, topicName));

					
					Map<String, Object> editExamTopicMapping = dispatcher.runSync("editExamTopicMapping",
							UtilMisc.toMap(OnlineExam.EXAM_ID, examId, OnlineExam.TOPIC_ID, topicIdFromFrontEnd, OnlineExam.PERCENTAGE, percentage,
									OnlineExam.TOPIC_PASS_PERCENTAGE, passPercentage));

				} catch (GenericServiceException e) {
					String errMsg = "unable to update records in topicMaster ans examTopicMapping " + e.toString();
					request.setAttribute("Error", errMsg);
					return OnlineExam.ERROR;
				}
				request.setAttribute("EVENT_MESSAGE", "ExamTopicMapping updated successfully");
				return OnlineExam.SUCCESS;
			}
			String errMsg = "could not find examId " + examId;
			request.setAttribute("Error", errMsg);
			return OnlineExam.ERROR;
		}

		if (UtilValidate.isEmpty(topicName)) {
			String errMsg = "topicName is required field";
			request.setAttribute("ERROR_MESSAGE", errMsg);
			return OnlineExam.ERROR;
		}
		try {
			
			Map<String, Object> topicDetails = dispatcher.runSync("createTopicMaster",
					UtilMisc.toMap(OnlineExam.TOPIC_NAME, topicName));
			String topicIdForCreation = (String) topicDetails.get(OnlineExam.TOPIC_ID);

			Map<String, Object> examTopicMappingDetails  = dispatcher.runSync("examTopicMapping", UtilMisc.toMap("examId", examId,
					OnlineExam.TOPIC_ID, topicIdForCreation, OnlineExam.PERCENTAGE, percentage, OnlineExam.TOPIC_PASS_PERCENTAGE, passPercentage));
			request.setAttribute(OnlineExam.TOPIC_NAME, topicName);
			request.setAttribute("EVENT_MESSAGE", examTopicMappingDetails);

		} catch (GenericServiceException e) {
			String errMsg = "Unable to create new records in TopicMaster entity: " + e.toString();
			request.setAttribute("ERROR_MESSAGE", errMsg);
			return OnlineExam.ERROR;
		}
		request.setAttribute("EVENT_MESSAGE", "ExamTopicMapping created successfully");
		return OnlineExam.SUCCESS;

	}
}
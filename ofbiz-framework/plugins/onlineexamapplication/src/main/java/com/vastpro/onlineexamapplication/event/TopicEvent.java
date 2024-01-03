package com.vastpro.onlineexamapplication.event;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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

public class TopicEvent {
	public static final String module = TopicEvent.class.getName();

		
	public static String createTopicMaster(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);

		String examId = (String) request.getAttribute(OnlineExam.EXAM_ID);
		String topicIdFromFrontEnd = (String) request.getAttribute(OnlineExam.TOPIC_ID);

		String topicName = (String) request.getAttribute(OnlineExam.TOPIC_NAME);
		String percentage = (String) request.getAttribute(OnlineExam.PERCENTAGE);
		String passPercentage = (String) request.getAttribute(OnlineExam.TOPIC_PASS_PERCENTAGE);
		
		BigDecimal percentageFromEntity=null;
		int totalPercentageValue=0;
		List<Integer>topicsPercentage=new LinkedList<>();
		List<GenericValue> topicsList=new ArrayList<>();
		int noOfQuestions=0;
		if(UtilValidate.isNotEmpty(examId)) {
			try {
				topicsList= EntityQuery.use(delegator).from("ExamTopicMappingMaster").where("examId",examId).cache().queryList();
				for(GenericValue listOfTopics:topicsList) {
					percentageFromEntity=(BigDecimal) listOfTopics.get("percentage");
					int listPercentage=percentageFromEntity.intValue();
					
					topicsPercentage.add(listPercentage);
					totalPercentageValue=totalPercentageValue+listPercentage;
				}
				GenericValue questionsPerExam=EntityQuery.use(delegator).from("ExamMaster").where("examId",examId).cache().queryOne();
				Long questions=(Long) questionsPerExam.get("noOfQuestions");
				noOfQuestions=questions.intValue();
				int remainingPercentage=noOfQuestions-totalPercentageValue;
				System.out.println("this is the remaining percentage"+remainingPercentage);
				System.out.println("this is the no of questions of the exam "+noOfQuestions);
				
			} catch (GenericEntityException e) {
				e.printStackTrace();
			}
			System.out.println("this is the topics list of a exam "+topicsList);
			System.out.println("this is the total percentage of the topics"+totalPercentageValue);
		}
		
		if(totalPercentageValue<noOfQuestions) {
		if (UtilValidate.isNotEmpty(topicIdFromFrontEnd)) {
			if (UtilValidate.isNotEmpty(examId)) {
				try {
					Debug.logInfo(
							"=======updating TopicMaster record in event using service createTopicMaster=========",
							module);
					Map<String, Object> editTopicMaster = dispatcher.runSync("editTopicMaster",
							UtilMisc.toMap(OnlineExam.TOPIC_ID, topicIdFromFrontEnd, OnlineExam.TOPIC_NAME, topicName));

					Debug.logInfo(
							"=======updating ExamTopicMapping record in event using service createTopicMaster=========",
							module);
					Map<String, Object> editExamTopicMapping = dispatcher.runSync("editExamTopicMapping",
							UtilMisc.toMap(OnlineExam.EXAM_ID, examId, OnlineExam.TOPIC_ID, topicIdFromFrontEnd, OnlineExam.PERCENTAGE, percentage,
									OnlineExam.TOPIC_PASS_PERCENTAGE, passPercentage));

				} catch (GenericServiceException e) {
					String errMsg = "unable to update records in topicMaster or examTopicMapping " + e.toString();
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
			Debug.logInfo("=======Creating TopicMaster record in event using service createTopicMaster=========",
					module);
			Map<String, Object> topicDetails = dispatcher.runSync("createTopicMaster",
					UtilMisc.toMap(OnlineExam.TOPIC_NAME, topicName));
			String topicIdForCreation = (String) topicDetails.get(OnlineExam.TOPIC_ID);

			Debug.logInfo("=======Creating ExamTopicMapping record in event using service createTopicMaster=========",
					module);
			Map<String, Object> examTopicMappingDetails  = dispatcher.runSync("examTopicMapping", UtilMisc.toMap("examId", examId,
					OnlineExam.TOPIC_ID, topicIdForCreation, OnlineExam.PERCENTAGE, percentage, OnlineExam.TOPIC_PASS_PERCENTAGE, passPercentage));
			request.setAttribute(OnlineExam.TOPIC_NAME, topicName);
			request.setAttribute("EVENT_MESSAGE", examTopicMappingDetails);

		} catch (GenericServiceException e) {
			String errMsg = "Unable to create new records in TopicMaster entity: " + e.toString();
			request.setAttribute("ERROR_MESSAGE", errMsg);
			return OnlineExam.ERROR;
		}
		
		}else {
			String errMsg="topic cannot be created because percentage of the exam is less than topic percentage";
			request.setAttribute("EVENT_MESSAGE", errMsg);
		}
		request.setAttribute("EVENT_MESSAGE", "ExamTopicMapping created successfully");
		return OnlineExam.SUCCESS;
	}
	
	public static String getTopicMaster(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);

		String result = null;

		String topicId = request.getParameter("topicId");
		String examId = request.getParameter("examId");

		if (UtilValidate.isNotEmpty(topicId) || UtilValidate.isNotEmpty(examId) || topicId != null || examId != null) {
			try {
				GenericValue ExamTopicMappingMasterDetail = EntityQuery.use(delegator).from("ExamTopicMappingMaster")
						.where("examId", examId, "topicId", topicId).cache().queryOne();
				GenericValue topicMasterDetail = EntityQuery.use(delegator).from("TopicMaster")
						.where("topicId", topicId).cache().queryOne();
				request.setAttribute("TopicDetails", ExamTopicMappingMasterDetail);// ExamTopicMappingMasterDetail
				request.setAttribute("TopicName", topicMasterDetail);// topicMasterDetail
			} catch (GenericEntityException e) {
				String errMsg = "unable to get topicDetails from entity ExamTopicMappingMaster and TopicMaster"
						+ e.toString();
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}

			result = OnlineExam.SUCCESS;
			request.setAttribute("result", result);
			return OnlineExam.SUCCESS;
		}
		String errMsg = "examId or topicId is null or empty";
		request.setAttribute("errMsg", errMsg);
		result = OnlineExam.ERROR;
		request.setAttribute("result", result);
		return OnlineExam.ERROR;
	}
	
	
	public static String deleteTopic(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		String examId = request.getParameter("examId");
		String topicId = request.getParameter("topicId");
		String result = OnlineExam.NULL;

		if (UtilValidate.isNotEmpty(topicId) || UtilValidate.isNotEmpty(examId) || topicId != OnlineExam.NULL
				|| examId != OnlineExam.NULL) {
			try {

				GenericValue deleteExamTopicMappingMaster = EntityQuery.use(delegator).from("ExamTopicMappingMaster")
						.where("examId", examId, "topicId", topicId).cache().queryOne();

				if (UtilValidate.isNotEmpty(deleteExamTopicMappingMaster)) {

					List<GenericValue> questionMaster = EntityQuery.use(delegator).from("QuestionMaster")
							.where("topicId", topicId).cache().queryList();
					Long questionId = null;

					if (UtilValidate.isNotEmpty(questionMaster)) {
						for (GenericValue qm : questionMaster) {
							Map<String, Object> checkQuestion = new HashMap<String, Object>();
							questionId = (Long) qm.get("questionId");
							checkQuestion.put("questionId", questionId);
							dispatcher.runSync("DeleteQuestionService", checkQuestion);
						}
					}
					Map<String, Object> deleteExamTopicMapping = dispatcher.runSync("deleteExamTopicMapping",
							UtilMisc.toMap("examId", examId, "topicId", topicId));

					if (ServiceUtil.isSuccess(deleteExamTopicMapping)) {
						Map<String, Object> deleteTopicMaster = dispatcher.runSync("deleteTopicMaster",
								UtilMisc.toMap("topicId", topicId));
					} else {
						request.setAttribute("service error ", "service(deleteTopicMaster) failed");
					}
					String eventMsg = "topic Deleted Success Fully";
					request.setAttribute("errMsg", eventMsg);
					result = OnlineExam.SUCCESS;
					request.setAttribute("result", result);
					return OnlineExam.SUCCESS;
				} else {
					String errMsg = "examId topicId not there in ExamTopicMappingMaster";
					result = OnlineExam.ERROR;
					request.setAttribute("result", result);
					request.setAttribute("errMsg", errMsg);
					return OnlineExam.ERROR;
				}

			} catch (GenericEntityException e) {
				String errMsg = "unable to get record from ExamTopicMappingMaster" + e.toString();
				request.setAttribute("errMsg", errMsg);
				return OnlineExam.ERROR;
			} catch (GenericServiceException e) {
				String errMsg = "unable to get record from ExamTopicMappingMaster" + e.toString();
				request.setAttribute("errMsg", errMsg);
				return OnlineExam.ERROR;
			}
		}

		String errMsg = "examId and topicId are requied fields";
		request.setAttribute("errMsg", errMsg);
		return OnlineExam.ERROR;
	}
	
	public static String topicList(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);

		GenericValue topic = null;
		List<GenericValue> listOfExamTopicMapping = null;
		List<Map<String, Object>> topicDetails = new LinkedList<>();
		String topicId = null;

		String examId = request.getParameter("examId");
		if (UtilValidate.isNotEmpty(examId)) {
			try {
				listOfExamTopicMapping = EntityQuery.use(delegator).from("ExamTopicMappingMaster")
						.where("examId", examId).cache().queryList();

				for (GenericValue ExamTopicMapping : listOfExamTopicMapping) {
					Map<String, Object> map = new HashMap<String, Object>();
					topicId = ExamTopicMapping.getString("topicId");
					topic = EntityQuery.use(delegator).from("TopicMaster").where("topicId", topicId).cache().queryOne();
					map.put("topicId", topicId);
					map.put("topicName", topic.getString("topicName"));
					topicDetails.add(map);
				}
			} catch (GenericEntityException e) {
				String errMsg = "unable to get record from ExamTopicMappingMaster" + e.toString();
				request.setAttribute("errMsg", errMsg);
				return OnlineExam.ERROR;
			}
			request.setAttribute("topicList", topicDetails);
			return OnlineExam.SUCCESS;
		}
		String errMsg = "examId is requied field";
		request.setAttribute("errMsg", errMsg);
		return OnlineExam.ERROR;
	}
	
}
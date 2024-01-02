package com.vastpro.onlineexamapplication.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class DeleteTopic {
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
}
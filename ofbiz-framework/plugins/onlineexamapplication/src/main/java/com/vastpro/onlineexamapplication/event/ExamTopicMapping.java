package com.vastpro.onlineexamapplication.event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.LocalDispatcher;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class ExamTopicMapping {

	public static String examTopic(HttpServletRequest request, HttpServletResponse response) {

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
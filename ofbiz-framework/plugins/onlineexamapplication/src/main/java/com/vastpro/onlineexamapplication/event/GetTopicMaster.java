package com.vastpro.onlineexamapplication.event;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class GetTopicMaster {

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
}
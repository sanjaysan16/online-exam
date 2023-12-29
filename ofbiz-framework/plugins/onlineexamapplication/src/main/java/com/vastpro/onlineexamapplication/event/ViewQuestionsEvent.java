package com.vastpro.onlineexamapplication.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class ViewQuestionsEvent {

	public static String viewQuestions(HttpServletRequest request, HttpServletResponse response) {

		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		
		
	  String topicId=request.getParameter(OnlineExam.TOPIC_ID);

		 
		if(UtilValidate.isNotEmpty(topicId)) {

			List<GenericValue> questionList = new ArrayList<GenericValue>();
			

			try {
				questionList = EntityQuery.use(delegator).from(OnlineExam.QuestionMaster).where(OnlineExam.TOPIC_ID,topicId).cache().queryList();
			} catch (GenericEntityException e) {
				String err = "unable to connect QuestionMaster entity" + e.toString();
				request.setAttribute("error", err);
				return OnlineExam.ERROR;
			}

			if (UtilValidate.isEmpty(questionList)|| questionList == null) {
				String err = "There is no  Question In This topic";
				request.setAttribute("error", err);

				return OnlineExam.ERROR;
			}
			request.setAttribute("questionList", questionList);

			return OnlineExam.SUCCESS;

		}
		
		String errMsg="TopicId iss Null or empty";
        request.setAttribute("errMsg", errMsg);
		return OnlineExam.ERROR;

	}

}

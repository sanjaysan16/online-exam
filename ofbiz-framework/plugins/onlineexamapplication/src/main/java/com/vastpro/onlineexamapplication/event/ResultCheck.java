package com.vastpro.onlineexamapplication.event;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class ResultCheck {
	public static final String module=ResultCheck.class.getName();
	
	public static String checkResult(HttpServletRequest request,HttpServletResponse response) {
		Delegator delegator=(Delegator)request.getAttribute("delegator");
		Map<String,Object> feilds=UtilHttp.getCombinedMap(request);
		String partyId=(String)request.getSession().getAttribute("partyId");
		String examName=request.getParameter("examName");
		Debug.logInfo("partyId",partyId,module);
		Debug.logInfo("examName",examName,module);
		
		if(UtilValidate.isNotEmpty(examName)) {
			try {
				GenericValue examDetails=EntityQuery.use(delegator).from("ExamMaster").where("examName",examName).cache().queryOne();
				String examId = (String) examDetails.get("examId");
				System.out.println("this is the exam details"+examDetails);
				Debug.logInfo("examId",examId,module);
				if(UtilValidate.isNotEmpty(examId)&&UtilValidate.isNotEmpty(partyId)) {
					GenericValue userAttemptDetails=EntityQuery.use(delegator).from("UserAttemptMaster").where("partyId",partyId,"examId",examId).cache().queryOne();
					if(UtilValidate.isNotEmpty(userAttemptDetails)) {
						int noOfQuestions = (int)userAttemptDetails.get("noOfQuestions");
						int totalCorrect=(int)userAttemptDetails.get("totalCorrect");
						int totalWrong=(int)userAttemptDetails.get("totalWrong");
						request.setAttribute("noOfQuestions", noOfQuestions);
						request.setAttribute("totalCorrect", totalCorrect);
						request.setAttribute("totalWrong", totalWrong);
						if(UtilValidate.isNotEmpty(noOfQuestions) && UtilValidate.isNotEmpty(totalWrong)) {
							int total=noOfQuestions-totalWrong;
							request.setAttribute("total", total);
						}else {
							String errMsg="no of questions is empty in the userAttemptMaster entity";
							request.setAttribute("EVENT_MESSAGE", errMsg);
							return OnlineExam.ERROR;
						}
					}
				}
			} catch (GenericEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		return OnlineExam.SUCCESS;
	}
	
}

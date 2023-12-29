package com.vastpro.onlineexamapplication.event;



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.bcel.verifier.structurals.GenericArray;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

import com.vastpro.onlineexamapplication.constant.OnlineExam;
public class GetExamAndExamList {

	public static final String MODULE = GetExamAndExamList.class.getName();

	public static String getExamAndExamList(HttpServletRequest request, HttpServletResponse reponse) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		List<GenericValue> examList = null;
		GenericValue exam = null;
		String examId = (String) request.getAttribute("examId");
		if (examId != null) {
			try {
				Debug.logInfo("==========Geting exam from Exam entity==========", MODULE);
				exam = EntityQuery.use(delegator).from("ExamMaster").where("examId", examId).cache().queryOne();
				if(exam==null) {
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
				Debug.logInfo("==========Geting examList from Exam entity==========", MODULE);
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
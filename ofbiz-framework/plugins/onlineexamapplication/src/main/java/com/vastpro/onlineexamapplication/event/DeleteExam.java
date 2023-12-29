package com.vastpro.onlineexamapplication.event;

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


public class DeleteExam {
	public static final String module = DeleteExam.class.getName();
	
	public static String deleteExam(HttpServletRequest request,HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher= (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin= (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);
		
		String examId= (String)request.getAttribute(OnlineExam.EXAM_ID);
	
		if(UtilValidate.isEmpty(examId)){
			String errMsg="could not find examId as "+examId;
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}
		
		try {
			try {
				
				GenericValue examGv= EntityQuery.use(delegator).from(OnlineExam.Exam_Master).where(OnlineExam.EXAM_ID,examId).cache().queryOne();
			
				if(UtilValidate.isNotEmpty(examId)) {
					List<GenericValue> examTopicMappingListGv= EntityQuery.use(delegator).from(OnlineExam.EXAM_TOPIC_MAPPING_MASTER).where(OnlineExam.EXAM_ID,examId).cache().queryList();

					if(UtilValidate.isNotEmpty(examTopicMappingListGv)) {
						for(GenericValue examTopicMapping:examTopicMappingListGv) {
							String topicId=(String)examTopicMapping.get(OnlineExam.TOPIC_ID);
							List<GenericValue> ListOfQuestions= EntityQuery.use(delegator).from(OnlineExam.Question_Master).where( OnlineExam.TOPIC_ID ,topicId).cache().queryList();
                            
						   if(UtilValidate.isNotEmpty(ListOfQuestions)) {
							   
							   for(GenericValue question:ListOfQuestions) {
								   Long questionId=(Long)question.get(OnlineExam.QUESTION_ID);
									 dispatcher.runSync("DeleteQuestionService", UtilMisc.toMap(OnlineExam.QUESTION_ID,questionId,OnlineExam.USERLOGIN,userLogin));
							   }
						   }
							Map<String,Object> deleteExamTopicMapping= dispatcher.runSync("deleteExamTopicMapping", UtilMisc.toMap(OnlineExam.EXAM_ID,examId,OnlineExam.TOPIC_ID,topicId,OnlineExam.USERLOGIN,userLogin));
							
                            
							 if(ServiceUtil.isSuccess(deleteExamTopicMapping)) {
									Map<String,Object> deleteTopicMaster= dispatcher.runSync("deleteTopicMaster", UtilMisc.toMap(OnlineExam.TOPIC_ID,topicId,OnlineExam.USERLOGIN,userLogin));
							 }
							 else {
								 request.setAttribute("service_error", "service(deleteTopicMaster) failed");
								 return OnlineExam.ERROR;
							 }
						}
					}
					dispatcher.runSync("deleteExam",UtilMisc.toMap(OnlineExam.EXAM_ID,examId,OnlineExam.USERLOGIN,userLogin));
					request.setAttribute("_EVENT_MESSAGE", "Exam deleted succesfully.");
					return OnlineExam.SUCCESS;
				}
				else {
					String errMsg="unable to find exam from Exam entity";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
			} catch (GenericEntityException e) {
				String errMsg="unable to get record from Exam entity"+e.toString();
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
		} catch (GenericServiceException e) {
			String errMsg="unable to delete record from Exam entity"+e.toString();
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}
		
	}
}
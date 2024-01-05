package com.vastpro.onlineexamapplication.event;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
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
import com.vastpro.onlineexamapplication.forms.TopicValidator;
import com.vastpro.onlineexamapplication.forms.check.TopicCheck;
import com.vastpro.onlineexamapplication.helper.HibernateHelper;

public class TopicEvent {
	public static final String module = TopicEvent.class.getName();
	public static String resource_error = "OnlineexamapplicationUiLabels";
	
	public static String createTopicMaster(HttpServletRequest request,HttpServletResponse response) {
		Delegator delegator=(Delegator)request.getAttribute("delegator");
		LocalDispatcher dispatcher=(LocalDispatcher) request.getAttribute("dispatcher");
		
		/*
		 * Input fields from front end
		 */
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		String examId=(String)combinedMap.get("examId");
		String topicId=(String)combinedMap.get("topicId");
		String topicName=(String)combinedMap.get("topicName");
		String percentage=(String)combinedMap.get("percentage");
		String topicPassPercentage=(String)combinedMap.get("topicPassPercentage");
		int percentageInInt=Integer.parseInt(percentage);
		
		Locale locale=UtilHttp.getLocale(request);
		/*
		 * Hibernate validation
		 */
		Map<String,Object> feildMap=UtilMisc.toMap("topicName",topicName,"percentage",percentage,"topicPassPercentage",topicPassPercentage);
		TopicValidator beanFromMap = HibernateHelper.populateBeanFromMap(feildMap, TopicValidator.class);
		Set<ConstraintViolation<TopicValidator>> checkValidationErrors = HibernateHelper.checkValidationErrors(beanFromMap, TopicCheck.class);
		boolean validateFormSubmission = HibernateHelper.validateFormSubmission(delegator, checkValidationErrors, request, locale, "TopicFeilds error message", resource_error, false);
		request.setAttribute("validateFormSubmission", validateFormSubmission);
		
		/*
		 * 
		 */
		
		Long noOfQuestions=(long) 0;
		int totalPercentage=0;
		int remainingPercentage=0;
		int finalPercentage=0;
		if(validateFormSubmission==false) {
			
			if(UtilValidate.isNotEmpty(examId)) {
				try {
					GenericValue noOfQuestionFromExam = EntityQuery.use(delegator).from("ExamMaster").where("examId",examId).cache().queryOne();
					if(UtilValidate.isNotEmpty(noOfQuestionFromExam)) {
						noOfQuestions=(Long) noOfQuestionFromExam.get("noOfQuestions");
						Debug.logInfo("this is the no of questions of the exam :"+noOfQuestions,module);
					}else {
						String errMsg="examId is exam entity is empty";
						request.setAttribute("EVENT_MESSAGE", errMsg);
						return OnlineExam.ERROR;
					}
					BigDecimal topicPercentageFromEntity=null;
					List<Integer> topicsPercentageOfWholeExam=new LinkedList<>();
					List<GenericValue> topicsList = EntityQuery.use(delegator).from("ExamTopicMappingMaster").where("examId",examId).cache().queryList();
					for(GenericValue Topics:topicsList) {
						topicPercentageFromEntity=(BigDecimal)Topics.get("percentage");
						int topicPercentageFromEntityInInt=topicPercentageFromEntity.intValue();
						topicsPercentageOfWholeExam.add(topicPercentageFromEntityInInt);
						totalPercentage=totalPercentage+topicPercentageFromEntityInInt;
					}
					remainingPercentage= 100-totalPercentage;
					Debug.logInfo("this is the total percentage of the topics in entity :"+totalPercentage,module);
					Debug.logInfo("this is the remaining percentage to create or edit topic"+ remainingPercentage,module);
				} catch (GenericEntityException e1) {
					Debug.logInfo(e1.getMessage(), module);
				}
				/*
				 * here we create the topicMaster and examTopicMappingMaster 
				 */
				int topicPercentageInInt=Integer.parseInt(percentage);
				if(topicId.isEmpty()) {
					
				
				if(topicPercentageInInt<=remainingPercentage) {
					int topicPercentageCaluclation=(int) (topicPercentageInInt*noOfQuestions);
					int finalTopicPercentage=topicPercentageCaluclation/100;
				try {
					Map<String, Object> createTopic = dispatcher.runSync("createTopicMaster", UtilMisc.toMap("topicName",topicName,"percentage",percentage,"topicPassPercentage",topicPassPercentage));
					System.out.println("this is the result of createTopicMaster :"+createTopic);
					if(ServiceUtil.isSuccess(createTopic)) {
						String topicIdFromEntity=(String)createTopic.get("topicId");
						if(UtilValidate.isNotEmpty(topicIdFromEntity)) {
							Map<String, Object> createExamTopicMapping = dispatcher.runSync("examTopicMapping", UtilMisc.toMap("examId",examId,"topicId",topicIdFromEntity,"percentage",percentage,"topicPassPercentage",topicPassPercentage,"questionsPerExam",finalTopicPercentage));
							System.out.println("this is the result of createExamTopicMapping :"+createExamTopicMapping);
							if(ServiceUtil.isSuccess(createExamTopicMapping)) {
								request.setAttribute("EVENT_MESSAGE", "ExamTopicMapping created successfully");
							}
						}else {
							String errMsg="topic id is empty from entity topic master while creation";
							request.setAttribute("EVENT_MESSAGE", errMsg);
							return OnlineExam.ERROR;
						}
					}
				} catch (GenericServiceException e) {
					Debug.logError(e.getMessage(), module);
				}
				}else {
					String errMsg="you can't create the topic because the remaining topicPercentage is : "+remainingPercentage;
					request.setAttribute("EVENT_MESSAGE", errMsg);
					return OnlineExam.ERROR;
				}
				request.setAttribute("EVENT_MESSAGE", "ExamTopicMapping created successfully");
				}
				/*
				 * edit topic and examTopicMapping
				 */
					if(UtilValidate.isNotEmpty(topicId) && UtilValidate.isNotEmpty(examId)) {
						try {
							GenericValue topicInExamTopicMapping=EntityQuery.use(delegator).from("ExamTopicMappingMaster").where("examId",examId,"topicId",topicId).cache().queryOne();
							BigDecimal topicPercentageForEdit=(BigDecimal)topicInExamTopicMapping.get("percentage");
							int topicPercentageForEditInInt=topicPercentageForEdit.intValue();
							Debug.logInfo("this is the percentage from entity" + topicPercentageForEditInInt,module);
							Debug.logInfo("this is the value of the totalPercentage" + totalPercentage,module);
							int remainingPercentageForEdit=totalPercentage-topicPercentageForEditInInt;
							int percentageForEdit=100-remainingPercentageForEdit;
							Debug.logInfo("this is the parcentage after the edit"+percentageForEdit,module);
							int noOfQuestionsForEditCaluclation=(int) (percentageInInt*noOfQuestions);
							int noOfQuestionsForEdit=noOfQuestionsForEditCaluclation/100;
							Debug.logInfo("this is the remaining percentage for edit" + remainingPercentageForEdit,module);
							Debug.logInfo("this is the no of questions for edit "+ noOfQuestionsForEdit,module);
							if(percentageInInt<=percentageForEdit) {
								Map<String, Object> editTopicMaster = dispatcher.runSync("editTopicMaster", UtilMisc.toMap("topicId",topicId,"topicName",topicName));
								if(ServiceUtil.isSuccess(editTopicMaster)) {
									Map<String, Object> editExamTopicMapping = dispatcher.runSync("editExamTopicMapping", UtilMisc.toMap("examId",examId,"topicId",topicId,"percentage",percentage,"topicPassPercentage",topicPassPercentage,"questionsPerExam",noOfQuestionsForEdit));
									if(ServiceUtil.isSuccess(editExamTopicMapping)) {
										request.setAttribute("EVENT_MESSAGE", "ExamTopicMapping updated successfully");
									}
								}else {
									request.setAttribute("EVENT_MESSAGE", editTopicMaster);
									return OnlineExam.ERROR;
								}
							}else {
								String errMsg="you cannot edit the topic because remaining percentage :"+percentageForEdit;
								request.setAttribute("EVENT_MESSAGE", errMsg);
								return OnlineExam.ERROR;
							}
						} catch (GenericEntityException e) {
							e.printStackTrace();
						} catch (GenericServiceException e) {
							e.printStackTrace();
						}
					}else {
						String errMsg="topicId is empty from frontEnd";
						request.setAttribute("EVENT_MESSAGE", errMsg);
						return OnlineExam.ERROR;
					}
					
				
			}else {
				String errMsg="examId is empty for creating topic";
				request.setAttribute("EVENT_MESSAGE", errMsg);
				return OnlineExam.ERROR;
			}
		}else {
			String errMsg="please enter required feilds";
			request.setAttribute("EVENT_MESSAGE", errMsg);
			return OnlineExam.ERROR;
		}
		
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
				request.setAttribute("EVENT_MESSAGE", errMsg);
				return OnlineExam.ERROR;
			}

			result = OnlineExam.SUCCESS;
			request.setAttribute("result", result);
			return OnlineExam.SUCCESS;
		}
		String errMsg = "examId or topicId is null or empty";
		request.setAttribute("EVENT_MESSAGE", errMsg);
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
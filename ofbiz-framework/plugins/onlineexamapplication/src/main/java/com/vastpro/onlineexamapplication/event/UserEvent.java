package com.vastpro.onlineexamapplication.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.calcite.runtime.HttpUtils;
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

import com.ctc.wstx.shaded.msv.org_isorelax.dispatcher.Dispatcher;
import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class UserEvent {

	public static final String module = UserEvent.class.getName();

	public static String getUsersOfAdmin(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

		String partyIdOfAdmin = (String) userLogin.get("partyId");

		List<GenericValue> listOfUsersPartyId = null;
		List<Map<String, Object>> listOfUserDetails = new ArrayList<>();
//		Map<String, Object> listOfUserDetails = new HashMap<>();
		List<GenericValue> partyRoleOfAdmin;

		if (UtilValidate.isEmpty(partyIdOfAdmin)) {
			String errMsg = "partyIdOfAdmin is required";
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}

		try {
			partyRoleOfAdmin = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyIdOfAdmin).cache()
					.queryList();
			GenericValue partyRole = partyRoleOfAdmin.get(1);

			if (UtilValidate.isNotEmpty(partyRole)) {
				String roleTypeIdOfAdmin = (String) partyRole.get("roleTypeId");
				if (roleTypeIdOfAdmin.equals("adminExam")) {
					try {
						Debug.logInfo("=======Getting records from PartyRelationship=======", module);
						listOfUsersPartyId = EntityQuery.use(delegator).from("PartyRelationship")
								.where("partyIdFrom", partyIdOfAdmin).cache().queryList();

						if (UtilValidate.isEmpty(listOfUsersPartyId)) {
							request.setAttribute("ListOfUserDetails", listOfUserDetails);
							return OnlineExam.SUCCESS;
						}
						for (GenericValue userPartyId : listOfUsersPartyId) {
							String partyIdTo = (String) userPartyId.get("partyIdTo");
							GenericValue user = EntityQuery.use(delegator).from("Person").where("partyId", partyIdTo)
									.cache().queryOne();
							if (UtilValidate.isNotEmpty(user)) {
								// creating one map to put user details
								Map<String, Object> userDetailMap = new HashMap<String, Object>();

								String firstNameOfUser = (String) user.get("firstName");
								String lastNameOfUser = (String) user.get("lastName");
								String genderOfUser = (String) user.get("gender");
								String partyIdOfUser = (String) user.get("partyId");

								userDetailMap.put("firstName", firstNameOfUser);
								userDetailMap.put("lastName", lastNameOfUser);
								userDetailMap.put("gender", genderOfUser);
								userDetailMap.put("partyId", partyIdOfUser);

								GenericValue userLoginForStudent = EntityQuery.use(delegator).from("UserLogin")
										.where("partyId", partyIdTo).cache().queryOne();

								if (UtilValidate.isNotEmpty(userLoginForStudent)) {
									String userLoginIdForStudent = (String) userLoginForStudent.get("userLoginId");
									userDetailMap.put("userLoginId", userLoginIdForStudent);
									listOfUserDetails.add(userDetailMap);
								}
							}
						}
						if (UtilValidate.isNotEmpty(listOfUserDetails)) {
							request.setAttribute("ListOfUserDetails", listOfUserDetails);
							return OnlineExam.SUCCESS;
						}
					} catch (Exception e) {
						String errMsg = "unable to get records from PartyRelationship " + e.toString();
						request.setAttribute("ERROR", errMsg);
						return OnlineExam.ERROR;
					}
				}
				String errMsg = "given partyId is not a admin";
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
			String errMsg = "partyRoleOfAdmin is not there in PartyRole entity";
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;

		} catch (GenericEntityException e) {
			String errMsg = "unable to get records from partyRole entity";
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}
	}

	/**
	 * This will check a given partyId is user or not
	 * 
	 * @param request
	 * @param partyIdOfUser
	 * @return
	 */
	private static String checkStudentOrNot(HttpServletRequest request, String partyIdOfUser) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		List<GenericValue> userPartyRoleWithNa;
		if (UtilValidate.isNotEmpty(partyIdOfUser)) {
			try {
				// checking a given partyIdOfUser
				userPartyRoleWithNa = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyIdOfUser)
						.cache().queryList();
				GenericValue partyRoleOfuser = userPartyRoleWithNa.get(1);
				if (UtilValidate.isEmpty(partyRoleOfuser)) {
					String errMsg = "unable to find student for given partyId";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
				String roleTypeIdOfUser = (String) partyRoleOfuser.get("roleTypeId");
				if (roleTypeIdOfUser.equals("student")) {
					return OnlineExam.SUCCESS;
				} else {
					String errMsg = "given partyId is not a student";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
			} catch (GenericEntityException e) {
				String errMsg = "unable to connet to the PartyRole entity " + e.toString();
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
		} else {
			return OnlineExam.ERROR;
		}

	}

	/**
	 * createUserExamMapping
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String userExamMapping(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);

		Map<String, Object> userExamMap = UtilHttp.getCombinedMap(request);
		String partyIdOfUser = (String) userExamMap.get("partyIdOfUser");
		userExamMap.remove("partyIdOfUser");
		List<GenericValue> userPartyRoleWithNa;

		if (UtilValidate.isNotEmpty(partyIdOfUser)) {
			try {
				// checking a given partyIdOfUser
				userPartyRoleWithNa = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyIdOfUser)
						.cache().queryList();
				GenericValue partyRoleOfuser = userPartyRoleWithNa.get(1);
				if (UtilValidate.isEmpty(partyRoleOfuser)) {
					String errMsg = "unable to find User for given partyId";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
				String roleTypeIdOfUser = (String) partyRoleOfuser.get("roleTypeId");
				if (roleTypeIdOfUser.equals("student")) {
					// getting all AttributeNames from request

					Enumeration<String> chooseExams = request.getAttributeNames();

					// putting a chooseExams into the loop to get "examId"

					while (chooseExams.hasMoreElements()) {
						// creating CharSequence for get chosen examIds key from "chooseExams"

						CharSequence CharSequenceOfExamId = "choose-exam-";
						String examId = chooseExams.nextElement();

						// checking the examId contains CharSequenceOfExamId
						if (examId.contains(CharSequenceOfExamId)) {
							String examIdString = (String) userExamMap.get(examId);
							try {
								// checking a the examId is presented or not in ExamMaster entity
								GenericValue examDetails = EntityQuery.use(delegator).from(OnlineExam.Exam_Master)
										.where("examId", examIdString).cache().queryOne();
								// checking examDetails null or not
								if (UtilValidate.isEmpty(examDetails)) {
									String errMsg = "unable to find exam in the ExamMaster entity ";
									request.setAttribute("ERROR", errMsg);
									return OnlineExam.ERROR;
								}

								try {
									GenericValue UserExamMappingCheck = EntityQuery.use(delegator)
											.from("UserExamMappingMaster")
											.where("partyId", partyIdOfUser, "examId", examIdString).cache().queryOne();
									if (UtilValidate.isEmpty(UserExamMappingCheck)) {
										// calling service to create userExam
										Map<String, Object> createUserExamMapping = dispatcher.runSync(
												"createUserExamMapping",
												UtilMisc.toMap("partyId", partyIdOfUser, "examId", examIdString,
														"allowedAttempts", OnlineExam.ALLOWED_ATTEMPTS, "noOfAttempts",
														OnlineExam.NO_OF_ATTEMPTS, "timeoutDays",
														OnlineExam.TIMEOUT_DAYS, "passwordChangesAuto",
														OnlineExam.PASSWORD_CHANGES_AUTO, "canSplitExams",
														OnlineExam.CAN_SPLIT_EXAMS, "canSeeDetailedResults",
														OnlineExam.CAN_SEE_DETAILED_RESULTS, "maxSplitAttempts",
														OnlineExam.MAX_SPLIT_ATTEMPTS, "userLogin", userLogin));

										if (ServiceUtil.isError(createUserExamMapping)) {
											String errMsg = "createUserExamMapping sevice error";
											request.setAttribute("ERROR", errMsg);
											return OnlineExam.ERROR;
										}
									}

								} catch (GenericServiceException e) {
									String errMsg = "unable to connet to the UserExamMappingMaster entity "
											+ e.toString();
									request.setAttribute("ERROR", errMsg);
									return OnlineExam.ERROR;
								}

							} catch (GenericEntityException e) {
								String errMsg = "unable to connet to the ExamMaster entity " + e.toString();
								request.setAttribute("ERROR", errMsg);
								return OnlineExam.ERROR;
							}
						}
					}
				} else {
					String errMsg = "given partyId is not a user";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
			} catch (GenericEntityException e) {
				String errMsg = "unable to connet to the PartyRole entity " + e.toString();
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
			request.setAttribute("EVENT", "UserExamMapping success");
			return OnlineExam.SUCCESS;
		} else {
			String errMsg = "partyIdOfUser is empty ";
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}

	}

	/**
	 * getListOfUserExamMapping
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	public static String getListOfUserExamMapping(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);
		String partyIdOfUser = null;
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		String partyIdOfUserInUserLogin = (String) userLogin.get("partyId");
		List<GenericValue> listOfMappedExams = new ArrayList<GenericValue>();
		List<GenericValue> listOfUnMappedExams = new ArrayList<GenericValue>();
		Map<String, Object> mappedAndUnMappedExams = new HashMap<String, Object>();
		String userOrNot = null;
		if (UtilValidate.isNotEmpty(partyIdOfUserInUserLogin)) {
			userOrNot = checkStudentOrNot(request, partyIdOfUserInUserLogin);
			if (userOrNot.equals(OnlineExam.SUCCESS)) {
				partyIdOfUser = partyIdOfUserInUserLogin;
			} else {
				String partyIdOfUserInCombinedMap = (String) combinedMap.get("partyIdOfUser");
				partyIdOfUser = partyIdOfUserInCombinedMap;
				userOrNot = checkStudentOrNot(request, partyIdOfUserInCombinedMap);
			}
		} else {
			String errMsg = "pls login first";
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}

		if (userOrNot.equals(OnlineExam.SUCCESS)) {

			try {
				List<GenericValue> listOfUserExamMapping = EntityQuery.use(delegator).from("UserExamMappingMaster")
						.where("partyId", partyIdOfUser).cache().queryList();
				List<GenericValue> listOfExam = EntityQuery.use(delegator).from("ExamMaster").cache().queryList();

				if (UtilValidate.isNotEmpty(listOfUserExamMapping)) {
					if (UtilValidate.isNotEmpty(listOfExam)) {
						for (GenericValue exam : listOfExam) {
							boolean userExanMapChecker = false;
							String examIdOfListOfExam = (String) exam.get(OnlineExam.EXAM_ID);
							for (GenericValue UserExamMapping : listOfUserExamMapping) {
								String examIdOfUserExamMapping = (String) UserExamMapping.get(OnlineExam.EXAM_ID);
								if (examIdOfUserExamMapping.equals(examIdOfListOfExam)) {
									userExanMapChecker = true;
									listOfMappedExams.add(exam);
									break;
								}
							}
							if (!userExanMapChecker) {
								listOfUnMappedExams.add(exam);
							}
						}
						if (UtilValidate.isNotEmpty(listOfMappedExams) && UtilValidate.isNotEmpty(listOfExam)) {
							mappedAndUnMappedExams.put("listOfMappedExams", listOfMappedExams);
							mappedAndUnMappedExams.put("listOfUnMappedExams", listOfUnMappedExams);
							request.setAttribute("mappedAndUnMappedExams", mappedAndUnMappedExams);
							return OnlineExam.SUCCESS;
						}
					}
					String errMsg = "exams not found";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
				mappedAndUnMappedExams.put("listOfMappedExams", listOfMappedExams);
				mappedAndUnMappedExams.put("listOfUnMappedExams", listOfExam);
				request.setAttribute("mappedAndUnMappedExams", mappedAndUnMappedExams);
				return OnlineExam.SUCCESS;
			} catch (GenericEntityException e) {
				String errMsg = "unable to connet userExamMapping entity " + e.toString();
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
		}
		String errMsg = "no user found";
		request.setAttribute("ERROR", errMsg);
		return OnlineExam.ERROR;

	}

	/**
	 * getUserExamMapping
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 */
	public static String getUserExamMapping(HttpServletRequest request, HttpServletResponse reponse) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);
		String partyIdOfUser = (String) userLogin.get(OnlineExam.PARTY_ID);
		GenericValue userExamMapping = null;
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
		String examId = (String) combinedMap.get(OnlineExam.EXAM_ID);
		if (UtilValidate.isNotEmpty(examId) && UtilValidate.isNotEmpty(partyIdOfUser)) {
			try {
				Debug.logInfo("==========Geting UserExamMappingDetail from UserExamMapping entity==========", module);
				userExamMapping = EntityQuery.use(delegator).from("UserExamMappingMaster")
						.where(OnlineExam.EXAM_ID, examId, OnlineExam.PARTY_ID, partyIdOfUser).cache().queryOne();
				if (userExamMapping == null) {
					String errMsg = "no UserExamMapping founded from entity UserExamMapping";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
				request.setAttribute("getUserExamMapping", userExamMapping);
				return OnlineExam.SUCCESS;
			} catch (GenericEntityException e) {
				String errMsg = "unable to get record from entity UserExamMapping" + e.toString();
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
		}
		String errMsg = "either examId or partyId is null";
		request.setAttribute("ERROR", errMsg);
		return OnlineExam.ERROR;
	}

	/**
	 * getOrCreateUserAttempt
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 */
	public static String getOrCreateUserAttempt(HttpServletRequest request, HttpServletResponse reponse) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(OnlineExam.DISPATCHER);

		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);
		String partyIdOfUser = (String) userLogin.get(OnlineExam.PARTY_ID);

		GenericValue userExamMapping = null;
		GenericValue userAttemptMaster = null;
		GenericValue examDetail = null;
		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);
//		LocalDate currentDate = LocalDate.now();

		String examId = (String) combinedMap.get(OnlineExam.EXAM_ID);
		String performanceId = null;
//		String attemptNumber = null;
//		String score = null;
//		String completedDate = null;
//		String noOfQuestions = null;
//		String totalCorrect = "0";
//		String totalWrong = "0";
//		String userPassed = "Y";
		if (UtilValidate.isNotEmpty(examId) && UtilValidate.isNotEmpty(partyIdOfUser)) {
			try {
				examDetail = EntityQuery.use(delegator).from("ExamMaster").where(OnlineExam.EXAM_ID, examId).cache()
						.queryOne();
				if (UtilValidate.isEmpty(examDetail)) {
					String errMsg = "given examId not found in ExamMaster";
					request.setAttribute("Error", errMsg);
					return OnlineExam.ERROR;
				}
				try {
					Debug.logInfo("==========Geting UserAttemptMasterDetail from UserAttemptMaster entity==========",
							module);
					userExamMapping = EntityQuery.use(delegator).from("UserExamMappingMaster")
							.where(OnlineExam.EXAM_ID, examId, OnlineExam.PARTY_ID, partyIdOfUser).cache().queryOne();
					if (UtilValidate.isNotEmpty(userExamMapping)) {
						Long allowedAttempts = (Long) userExamMapping.get("allowedAttempts");
						Long noOfAttempts = (Long) userExamMapping.get("noOfAttempts");

						if (noOfAttempts < allowedAttempts) {
							noOfAttempts++;
							Long attemptNumber = noOfAttempts;
							userAttemptMaster = EntityQuery
									.use(delegator).from("UserAttemptMaster").where(OnlineExam.EXAM_ID, examId,
											OnlineExam.PARTY_ID, partyIdOfUser, "attemptNumber", attemptNumber)
									.cache().queryOne();
							if (UtilValidate.isNotEmpty(userAttemptMaster)) {
								if (UtilValidate.isEmpty(userAttemptMaster.get("completedDate"))) {
									String canSplitExams = (String) userExamMapping.get("canSplitExams");
									if (canSplitExams.equals("Y")) {
										request.setAttribute("Event", OnlineExam.SUCCESS);
										return OnlineExam.SUCCESS;
									}
									else {
										request.setAttribute("Event", "cannotSplitExam");
										return OnlineExam.SUCCESS;
									}
								}
							} else {
								try {
									Map<String, Object> createUserAttemptDetail = dispatcher
											.runSync("createUserAttempt",
													UtilMisc.toMap("examId", examId, "partyId", partyIdOfUser,
															"attemptNumber", attemptNumber, OnlineExam.USERLOGIN,
															userLogin));
									if (ServiceUtil.isSuccess(createUserAttemptDetail)) {

										performanceId = Integer
												.toString((int) createUserAttemptDetail.get("performanceId"));

										List<GenericValue> ExamTopicMappingList = EntityQuery.use(delegator)
												.from("ExamTopicMappingMaster").where("examId", examId).cache()
												.queryList();
										for (GenericValue ExamTopicMapping : ExamTopicMappingList) {
											String topicId = (String) ExamTopicMapping.get("topicId");
											Long questionsPerExam = (Long) ExamTopicMapping.get("questionsPerExam");
											BigDecimal topicPassPercentage = (BigDecimal) ExamTopicMapping
													.get("topicPassPercentage");
											// calling service user-attempt-topic-master
											dispatcher.runSync("user-attempt-topic-master",
													UtilMisc.toMap("performanceId", performanceId, "topicId", topicId,
															"topicPassPercentage", topicPassPercentage,
															"totalQuestionsInThisTopic", questionsPerExam));
										}
									} else {
										String errMsg = "examId from the ExamTopicMappingMaster is empty";
										request.setAttribute("EVENT_MESSAGE", errMsg);
										return OnlineExam.ERROR;
									}

								} catch (GenericServiceException e) {
									String errMsg = "unable to call service createUserAttempt" + e.toString();
									request.setAttribute("ERROR", errMsg);
									return OnlineExam.ERROR;
								}
								request.setAttribute("Event", OnlineExam.SUCCESS);
								return OnlineExam.SUCCESS;
							}
						} else {
							request.setAttribute("Event", "attemptLimitReachecd");
							return OnlineExam.SUCCESS;
						}
					} else {
						String errMsg = "unable to find userExamMapping from userExamMappingMaster";
						request.setAttribute("ERROR", errMsg);
						return OnlineExam.ERROR;
					}
				} catch (GenericEntityException e) {
					String errMsg = "unable to get record from entity UserAttemptMaster" + e.toString();
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
			} catch (GenericEntityException e1) {
				e1.printStackTrace();
			}
		}
		String errMsg = "either examId or partyId is null";
		request.setAttribute("ERROR", errMsg);
		return OnlineExam.ERROR;
	}
}
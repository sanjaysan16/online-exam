package com.vastpro.onlineexamapplication.event;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.bouncycastle.pqc.jcajce.provider.lms.LMSSignatureSpi.generic;

import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class UserEvent {

	public static final String module = UserEvent.class.getName();

	public static String getUsersOfAdmin(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);

		String partyIdOfAdmin = (String) request.getAttribute("partyIdOfAdmin");

		List<GenericValue> listOfUsersPartyId = null;
		List<Map<String,Object>> listOfUserDetails = new ArrayList<>();
//		Map<String, Object> listOfUserDetails = new HashMap<>();
		GenericValue partyRoleOfAdmin;

		if (UtilValidate.isEmpty(partyIdOfAdmin)) {
			String errMsg = "partyIdOfAdmin is required";
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}

		try {
			partyRoleOfAdmin = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyIdOfAdmin).cache()
					.queryOne();
			if (UtilValidate.isNotEmpty(partyRoleOfAdmin)) {
				String roleTypeIdOfAdmin = (String) partyRoleOfAdmin.get("roleTypeId");
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
								//creating one map to put user details
								Map<String, Object> userDetailMap=new HashMap<String, Object>();
								
								String firstNameOfUser = (String)user.get("firstName");
								String lastNameOfUser = (String)user.get("lastName");
								String genderOfUser = (String)user.get("gender");
								String partyIdOfUser = (String)user.get("partyId");
								
								userDetailMap.put("firstName", firstNameOfUser);
								userDetailMap.put("lastName", lastNameOfUser);
								userDetailMap.put("gender", genderOfUser);
								userDetailMap.put("partyId", partyIdOfUser);
								
								GenericValue userLogin = EntityQuery.use(delegator).from("UserLogin")
										.where("partyId", partyIdTo).cache().queryOne();
								
								if (UtilValidate.isNotEmpty(userLogin)) {
									String userLoginId = (String)userLogin.get("userLoginId");
									userDetailMap.put("userLoginId", userLoginId);
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
	private static String checkUserOrNot(HttpServletRequest request, String partyIdOfUser) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		GenericValue partyRoleOfuser;
		if (UtilValidate.isNotEmpty(partyIdOfUser)) {
			try {
				// checking a given partyIdOfUser
				partyRoleOfuser = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyIdOfUser).cache()
						.queryOne();
				if (UtilValidate.isEmpty(partyRoleOfuser)) {
					String errMsg = "unable to find User for given partyId";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
				String roleTypeIdOfUser = (String) partyRoleOfuser.get("roleTypeId");
				if (!roleTypeIdOfUser.equals("User")) {
					String errMsg = "given partyId is not a user";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
			} catch (GenericEntityException e) {
				String errMsg = "unable to connet to the PartyRole entity " + e.toString();
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
			return OnlineExam.SUCCESS;
		}
		return OnlineExam.ERROR;
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
		GenericValue partyRoleOfuser;

		if (UtilValidate.isNotEmpty(partyIdOfUser)) {
			try {
				// checking a given partyIdOfUser
				partyRoleOfuser = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyIdOfUser).cache()
						.queryOne();
				if (UtilValidate.isEmpty(partyRoleOfuser)) {
					String errMsg = "unable to find User for given partyId";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;
				}
				String roleTypeIdOfUser = (String) partyRoleOfuser.get("roleTypeId");
				if (!roleTypeIdOfUser.equals("User")) {
					String errMsg = "given partyId is not a user";
					request.setAttribute("ERROR", errMsg);
					return OnlineExam.ERROR;

				}
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
													OnlineExam.NO_OF_ATTEMPTS, "lastPerformanceDate",
													OnlineExam.LAST_PERFORMANCE_DATE, "timeoutDays",
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
								String errMsg = "unable to connet to the UserExamMappingMaster entity " + e.toString();
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

			} catch (GenericEntityException e) {
				String errMsg = "unable to connet to the PartyRole entity " + e.toString();
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
		}
		request.setAttribute("EVENT", "UserExamMapping success");
		return OnlineExam.SUCCESS;
	}

	public static String getListOfUserExamMapping(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
//		GenericValue userLogin = (GenericValue) request.getSession().getAttribute(OnlineExam.USERLOGIN);

		Map<String, Object> combinedMap = UtilHttp.getCombinedMap(request);

		String partyIdOfUser = (String) combinedMap.get("partyIdOfUser");
		String userOrNot = checkUserOrNot(request, partyIdOfUser);
		List<GenericValue> listOfMappedExams = new ArrayList<GenericValue>();
		List<GenericValue> listOfUnMappedExams = new ArrayList<GenericValue>();
		Map<String, Object> mappedAndUnMappedExams = new HashMap<String, Object>();
		if (userOrNot.equals(OnlineExam.SUCCESS)) {

			try {
				List<GenericValue> listOfUserExamMapping = EntityQuery.use(delegator).from("UserExamMappingMaster")
						.where("partyId", partyIdOfUser).cache().queryList();
				List<GenericValue> listOfExam = EntityQuery.use(delegator).from("ExamMaster").cache().queryList();

				if (UtilValidate.isNotEmpty(listOfUserExamMapping)) {
					if (UtilValidate.isNotEmpty(listOfExam)) {
						for (GenericValue exam : listOfExam) {
						   boolean userExanMapChecker=false;
							String examIdOfListOfExam = (String) exam.get(OnlineExam.EXAM_ID);
							for (GenericValue UserExamMapping : listOfUserExamMapping) {
								String examIdOfUserExamMapping = (String) UserExamMapping.get(OnlineExam.EXAM_ID);
								if (examIdOfUserExamMapping.equals(examIdOfListOfExam)) {
									userExanMapChecker=true;
									listOfMappedExams.add(exam);
									break;
								}
							}
							if(!userExanMapChecker) {
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
				String errMsg = "given partyId in not found in userExamMapping";
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
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

}
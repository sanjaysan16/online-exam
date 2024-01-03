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
		List<GenericValue> listOfUsers = new ArrayList<GenericValue>();
		List<GenericValue> listOfUserLogin = new ArrayList<GenericValue>();
		Map<String, Object> listOfUserDetails = new HashMap<>();

		if (UtilValidate.isEmpty(partyIdOfAdmin) || partyIdOfAdmin == null) {
			String errMsg = "partyIdOfAdmin is required";
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}

		try {
			Debug.logInfo("=======Getting records from PartyRelationship=======", module);
			listOfUsersPartyId = EntityQuery.use(delegator).from("PartyRelationship")
					.where("partyIdFrom", partyIdOfAdmin).cache().queryList();

			if (UtilValidate.isEmpty(listOfUsersPartyId) || listOfUsersPartyId == null) {
				String errMsg = "admin not founded from PartyRelationship";
				request.setAttribute("ERROR", errMsg);
				return OnlineExam.ERROR;
			}
			for (GenericValue userPartyId : listOfUsersPartyId) {
				String partyIdTo = (String) userPartyId.get("partyIdTo");

				GenericValue user = EntityQuery.use(delegator).from("Person").where("partyId", partyIdTo).cache()
						.queryOne();
				if (UtilValidate.isNotEmpty(user)) {
					GenericValue userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyIdTo)
							.cache().queryOne();
					if (UtilValidate.isNotEmpty(userLogin)) {
						listOfUsers.add(user);
						listOfUserLogin.add(userLogin);
					}
				}

			}
		} catch (Exception e) {
			String errMsg = "unable to get records from PartyRelationship " + e.toString();
			request.setAttribute("ERROR", errMsg);
			return OnlineExam.ERROR;
		}
		if (UtilValidate.isNotEmpty(listOfUsers) && UtilValidate.isNotEmpty(listOfUserLogin)) {
			listOfUserDetails.put("ListOfUsers", listOfUsers);
			listOfUserDetails.put("ListOfUserLoginId", listOfUserLogin);
			request.setAttribute("ListOfUserDetails", listOfUserDetails);
			return OnlineExam.SUCCESS;
		}

		String errMsg = "unable to get records";
		request.setAttribute("ERROR", errMsg);
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
}
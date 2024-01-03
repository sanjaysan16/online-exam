package com.vastpro.onlineexamapplication.event;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

import com.vastpro.onlineexamapplication.constant.OnlineExam;
import com.vastpro.onlineexamapplication.forms.HibernateValidator;
import com.vastpro.onlineexamapplication.forms.check.LoginCheck;
import com.vastpro.onlineexamapplication.helper.HibernateHelper;



public class UserLogin{
	public static String resource_error = "OnlineexamapplicationUiLabels";
	static String firstName=null;

	public static String login(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		GenericValue userDetails = null;

		Delegator delegator = (Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		Locale locale = UtilHttp.getLocale(request);
        Map<String, Object> fieldsMap = UtilHttp.getCombinedMap(request);
        String username = (String) fieldsMap.get("username");
		
		if(UtilValidate.isNotEmpty(username)||username!=null) {
			
			try {
				userDetails = EntityQuery.use(delegator).from("UserLogin")
						.where("userLoginId", username).cache().queryOne();
				
				if(UtilValidate.isNotEmpty(userDetails)) {
					
					//HYBERNATE VALIDATION	
					
					
					String password = (String) fieldsMap.get("password");
					
					
					String backEndPassword=(String) userDetails.get("currentPassword");
					if(password.equals(backEndPassword)) {
						
						Map<String, Object> userLoginMap = UtilMisc.toMap("username", username, "password", password);
						HibernateValidator fieldsValidator = HibernateHelper.populateBeanFromMap(userLoginMap,
								HibernateValidator.class);

						Set<ConstraintViolation<HibernateValidator>> errors = HibernateHelper.checkValidationErrors(fieldsValidator,
								LoginCheck.class);

						boolean hasFormErrors = HibernateHelper.validateFormSubmission(delegator, errors, request, locale,
								"MandatoryFieldErrMsgLoginForm", resource_error, false);
						request.setAttribute("hasFormErrors", hasFormErrors);
						
//						vALIDATION END
						
						String partyId = (String) userDetails.get("partyId");
						
						request.setAttribute("partyId", partyId);
						session.setAttribute("partyId", partyId);
						session.setAttribute("userLogin", userDetails);
						if(UtilValidate.isNotEmpty(partyId)||partyId!=null) {
							GenericValue rollType = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId).cache()
									.queryOne();
							String roll = (String) rollType.get("roleTypeId");

							GenericValue userNameDetails=EntityQuery.use(delegator).from("Person").where("partyId",partyId).cache().queryOne();
						    firstName=(String)userNameDetails.get("firstName");
							request.setAttribute("firstName",firstName);

							String flag = "false";
							if (UtilValidate.isNotEmpty(roll)) {
								if (roll.equalsIgnoreCase("adminExam")) {
									flag = "true";
								} else if (roll.equalsIgnoreCase("student")) {
									flag = "false";
								}
								request.setAttribute("isAdmin", flag);
								session.setAttribute("isAdmin", flag);
							}else {
								String errMsg="Role is not given for the user";
								request.setAttribute("ERROR_MESSAGE",errMsg);
								return OnlineExam.ERROR;
							}
						}else {
							String errMsg="partyId should not be empty";
							request.setAttribute("ERROR_MESSAGE",errMsg);
							return OnlineExam.ERROR;
						}
						
					}else {
						String errMsg="Password or username is wrong";
						request.setAttribute("ERROR_MESSAGE", errMsg);
						return OnlineExam.ERROR;
					}
				}else {
					String errMsg="User is not found";
					request.setAttribute("ERROR_MESSAGE",errMsg);
					return OnlineExam.ERROR;
				}
					
			} catch (GenericEntityException e) {
				String errMsg="unable to get userDetails";
				request.setAttribute("ERROR_MESSAGE",errMsg);
				return OnlineExam.ERROR;
			}
				
		}else {
			String errMsg="Username is empty or null";
			request.setAttribute("ERROR_MESSAGE",errMsg);
			return OnlineExam.ERROR;
		}
		
		request.setAttribute("Event_Msg","login successfull");
		return OnlineExam.SUCCESS;
	}
}
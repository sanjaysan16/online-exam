package com.vastpro.onlineexamapplication.event;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.webapp.control.LoginWorker;

import com.jcraft.jsch.Session;
import com.vastpro.onlineexamapplication.constant.OnlineExam;

public class Login {

	/*
	 * Method for login
	 */
	public static String login(HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		Delegator delegator=(Delegator) request.getAttribute(OnlineExam.DELEGATOR);
		Map<String, Object> fieldsMap = UtilHttp.getCombinedMap(request);

		String userName=(String)fieldsMap.get("USERNAME");
		String password=(String)fieldsMap.get("PASSWORD");
		boolean flag=false;
		if(UtilValidate.isNotEmpty("userName")) {
			String loginResult=LoginWorker.login(request, response);
			System.out.println("this is the login result"+loginResult);
			
			if(loginResult=="success") {
				boolean signInCheck=true;
				session.setAttribute("signInCheck", signInCheck);
				try {
					GenericValue userLogin=EntityQuery.use(delegator).from("UserLogin").where("userLoginId",userName).cache().queryOne();
					String partyId=(String) userLogin.get("partyId");
					if(UtilValidate.isNotEmpty(partyId)) {
						GenericValue partyRole=EntityQuery.use(delegator).from("PartyRole").where("partyId",partyId).cache().queryOne();
						String rollOfUser=(String) partyRole.get("roleTypeId");
						if(UtilValidate.isNotEmpty(rollOfUser)) {
							if(rollOfUser.equalsIgnoreCase("adminExam")) {
								 flag=true;
							}else {
								flag=false;
							}
							request.setAttribute("isAdmin", flag);
							session.setAttribute("isAdmin", flag);
							session.setAttribute("userLogin", userLogin);
							System.out.println("this is flag of user "+flag);
						}else {
							String errMsg="roll is not created for a user";
							request.setAttribute(" _ERROR_MESSAGE_", errMsg);
							return OnlineExam.ERROR;
						}
						
						
						
						
					}else {
						String errMsg="User is not found";
						request.setAttribute(" _ERROR_MESSAGE_", errMsg);
						return OnlineExam.ERROR;
					}
				} catch (GenericEntityException e) {
					e.printStackTrace();
				}
			}
			
		}else {
			String errMsg="please enter username userName is empty";
			request.setAttribute(" _ERROR_MESSAGE_", errMsg);
			return OnlineExam.ERROR;
		}
		
		return OnlineExam.SUCCESS;
	}
	
	
	/*
	 * Method for logout
	 */
	public static String logout(HttpServletRequest request,HttpServletResponse response) {
		boolean isUserLogin = false;
		request.getSession().getAttribute(OnlineExam.USERLOGIN);
		String logout = LoginWorker.logout(request, response);
		if(logout.equalsIgnoreCase("success")) {
		 isUserLogin= CheckLoginFlag.isUserlogin=true;
			
				request.setAttribute("isUserLogin", isUserLogin);
				return OnlineExam.SUCCESS;
		}
		String errMsg="user not logedOut";
		request.setAttribute("errMsg", errMsg);
		return OnlineExam.ERROR;
	}
	
}

package com.vastpro.onlineexamapplication.forms;
import org.hibernate.validator.constraints.NotEmpty;
import com.vastpro.onlineexamapplication.forms.check.LoginCheck;

public class LoginValidator {

	@NotEmpty(message="Username is empty",groups= {LoginCheck.class})
	//@Pattern(regexp="^[a-z0-9]+[@]{1}[a-z][.]{1}[a-z]$",message="Enter valid username",groups= {LoginCheck.class})
	@javax.validation.constraints.Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$", message = "invalidEmailFormat.errorMsg", groups = {
			LoginCheck.class })

	private String username;
	
	@NotEmpty(message="Password is empty",groups= {LoginCheck.class})
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
}
package com.vastpro.onlineexamapplication.forms;

import org.hibernate.validator.constraints.NotEmpty;

import com.vastpro.onlineexamapplication.forms.check.QuestionCheck;

public class QuestionValidators {
	@NotEmpty(message="Question name is empty",groups= {QuestionCheck.class})
	private String questionName;

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	
	
}

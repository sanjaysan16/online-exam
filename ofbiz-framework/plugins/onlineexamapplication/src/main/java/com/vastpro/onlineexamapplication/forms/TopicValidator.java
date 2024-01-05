package com.vastpro.onlineexamapplication.forms;


import org.hibernate.validator.constraints.NotEmpty;

import com.vastpro.onlineexamapplication.forms.check.TopicCheck;

public class TopicValidator {

	
	
	@NotEmpty(message="topicName is empty",groups= {TopicCheck.class})
	private String topicName;
	
	@NotEmpty(message="percentage is empty",groups= {TopicCheck.class})
	private String percentage;
	
	@NotEmpty(message="topicPassPercentage is empty",groups= {TopicCheck.class})
	private String topicPassPercentage;
	
	
	
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public String getTopicPassPercentage() {
		return topicPassPercentage;
	}
	public void setTopicPassPercentage(String topicPassPercentage) {
		this.topicPassPercentage = topicPassPercentage;
	}
	
	
	
}

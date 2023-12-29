package com.vastpro.onlineexamapplication.constant;

public interface OnlineExam {

	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String DELEGATOR = "delegator";
	public static final String DISPATCHER = "dispatcher";
	public static final String USERLOGIN = "userLogin";
	public static final String NULL = null;
	
	
	public static final String Exam_Master="ExamMaster";
	public static final String Question_Master="QuestionMaster";
	public static final String Topic_Master="TopicMaster";
    String EXAM_TOPIC_MAPPING_MASTER="ExamTopicMappingMaster";

	
	// Exam Master Fields
	public static final String EXAM_ID = "examId";
	public static final String EXAM_NAME = "examName";
	public static final String DESCRIPTION = "description";
	public static final String CREATION_DATE = "creationDate";
	public static final String EXPIRATION_DATE = "expirationDate";
	public static final String DURATION_MINUTES = "noOfQuestions";
	public static final String NO_OF_QUESTIONS = "durationMinutes";
	public static final String PASS_PERCENTAGE = "passPercentage";
	public static final String QUESTIONS_RANDOMIZED = "questionsRandomized";
	public static final String ANSWERS_MUST = "answersMust";
	public static final String ENABLE_NEGATIVE_MARK = "enableNegativeMark";
	public static final String DIFFICULTY_LEVEL = "difficultyLevel";
	public static final String ANSWER_VALUE = "answerValue";
	public static final String NEGATIVE_MARK_VALUE = "negativeMarkValue";
	public static final String PERCENTAGE="percentage";
	public static final String TOPIC_PASS_PERCENTAGE="topicPassPercentage";


	public static final String EXAMISALREDYEXIST = "Exam Is Alredy Exist";
	public static final String TOPICALREDYEXIST = "Topic Is Alredy Exist";
	public static final String QUESTIONALREDYEXIST = "Question Is Alredy Exist";
	public static final String EXAMNAMEISREQUIRED = "Exam Name Is Required";
	

	
	String TOPIC_ID = "topicId";
	String TOPIC_NAME = "topicName";
	String QUESTION_ID = "questionId";
	String QUESTION_DETAIL = "questionDetail";
	String OPTION_A = "optionA";
	String OPTION_B = "optionB";
	String OPTION_C = "optionC";
	String OPTION_D = "optionD";
	String OPTION_E = "optionE";
	String ANSWER = "answer";
	String NUM_ANSWERS = "numAnswers";
	String QUESTION_TYPE = "questionType";
	
	
	String PERFORMANCE_ID = "performanceId";
	String ATTEMPT_NUMBER = "attemptNumber";


}

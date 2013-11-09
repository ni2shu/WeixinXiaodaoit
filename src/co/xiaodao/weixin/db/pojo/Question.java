package co.xiaodao.weixin.db.pojo;

/**
 * 陪聊实体类，对应数据库表：tb_question
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class Question {

	// 问题分类
	private String category;
	// 问题
	private String question;
	// 问题答案
	private String answer;
	// 对应的表id
	private String id;
	// 第一次查询的用户openID
	private String openID;
	// 第一次查询时间
	private String createTime;

	public Question(String id, String category, String question, String answer,
			String openID, String createTime) {
		super();
		this.id = id;
		this.question = question;
		this.answer = answer;
		this.category = category;
		this.openID = openID;
		this.createTime = createTime;
	}
	
	public Question(String id, String category, String question, String answer) {
		super();
		this.id = id;
		this.question = question;
		this.answer = answer;
		this.category = category;
	}

	public Question(String category, String question, String answer,
			String openID, String createTime) {
		super();
		this.question = question;
		this.answer = answer;
		this.category = category;
		this.openID = openID;
		this.createTime = createTime;
	}

	public Question(String category, String question, String answer) {
		super();
		this.question = question;
		this.answer = answer;
		this.category = category;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}

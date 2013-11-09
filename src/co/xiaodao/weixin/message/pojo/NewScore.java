package co.xiaodao.weixin.message.pojo;

/**
 * 最新成绩
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-7-9
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class NewScore {
	private String class_name;
	private String xf;
	private String score;

	public NewScore() {
		super();
	}

	public NewScore(String class_name, String xf, String score) {
		super();
		this.class_name = class_name;
		this.xf = xf;
		this.score = score;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getXf() {
		return xf;
	}

	public void setXf(String xf) {
		this.xf = xf;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}

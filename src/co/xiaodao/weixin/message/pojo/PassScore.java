package co.xiaodao.weixin.message.pojo;

/**
 * 通过课程的每个课
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-5-6
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class PassScore {
	private String class_num;
	private String class_name;
	private String num;
	private String xf;
	private String kssj;
	private String score;
	private String ch_class_num;
	private String remark;

	public PassScore() {
		super();
	}
	
	public PassScore(String class_num, String class_name, String num,
			String xf, String kssj, String score, String ch_class_num,
			String remark) {
		super();
		this.class_num = class_num;
		this.class_name = class_name;
		this.num = num;
		this.xf = xf;
		this.kssj = kssj;
		this.score = score;
		this.ch_class_num = ch_class_num;
		this.remark = remark;
	}

	public String getClass_num() {
		return class_num;
	}

	public void setClass_num(String class_num) {
		this.class_num = class_num;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getXf() {
		return xf;
	}

	public void setXf(String xf) {
		this.xf = xf;
	}

	public String getKssj() {
		return kssj;
	}

	public void setKssj(String kssj) {
		this.kssj = kssj;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getCh_class_num() {
		return ch_class_num;
	}

	public void setCh_class_num(String ch_class_num) {
		this.ch_class_num = ch_class_num;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

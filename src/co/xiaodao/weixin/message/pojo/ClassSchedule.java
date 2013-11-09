package co.xiaodao.weixin.message.pojo;

/**
 * 课表
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-5-6
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ClassSchedule {

	private String class_name;
	private String class_num;
	private String kxh;
	private String kslx;
	private String addr;
	private String time;
	private String weekly;
	private String remark;

	public ClassSchedule() {
		super();
	}

	public ClassSchedule(String class_name, String class_num, String kxh,
			String kslx, String addr, String time, String weekly, String remark) {
		super();
		this.class_name = class_name;
		this.class_num = class_num;
		this.kxh = kxh;
		this.kslx = kslx;
		this.addr = addr;
		this.time = time;
		this.weekly = weekly;
		this.remark = remark;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getClass_num() {
		return class_num;
	}

	public void setClass_num(String class_num) {
		this.class_num = class_num;
	}

	public String getKxh() {
		return kxh;
	}

	public void setKxh(String kxh) {
		this.kxh = kxh;
	}

	public String getKslx() {
		return kslx;
	}

	public void setKslx(String kslx) {
		this.kslx = kslx;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWeekly() {
		return weekly;
	}

	public void setWeekly(String weekly) {
		this.weekly = weekly;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

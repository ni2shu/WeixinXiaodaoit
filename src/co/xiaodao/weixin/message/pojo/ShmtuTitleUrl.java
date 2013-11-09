package co.xiaodao.weixin.message.pojo;

/**
 * 对应标题和链接地址
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-27
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ShmtuTitleUrl {
	// 标题
	private String title;
	// 链接地址
	private String url;
	// 时间
	private String date;
	// 发布部门
	private String department;

	public ShmtuTitleUrl(String title, String url) {
		super();
		this.title = title;
		this.url = url;
	}

	public ShmtuTitleUrl(String title, String url, String date) {
		super();
		this.title = title;
		this.url = url;
		this.date = date;
	}

	public ShmtuTitleUrl(String title, String url, String date,
			String department) {
		super();
		this.title = title;
		this.url = url;
		this.date = date;
		this.department = department;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}

package co.xiaodao.weixin.db.pojo;

/**
 * 表白，对应数据库表：tb_show_love
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-5-12
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ShowLove {

	private String fromOpenID;
	private String fromName;
	private String toName;
	private String content;
	private String date;
	private String createDateTime;

	public ShowLove(String fromOpenID, String fromName, String toName,
			String content, String date, String createDateTime) {
		super();
		this.fromOpenID = fromOpenID;
		this.fromName = fromName;
		this.toName = toName;
		this.content = content;
		this.date = date;
		this.createDateTime = createDateTime;
	}

	public String getFromOpenID() {
		return fromOpenID;
	}

	public void setFromOpenID(String fromOpenID) {
		this.fromOpenID = fromOpenID;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

}

package co.xiaodao.weixin.message.pojo;

/**
 * 图书搜索的图书信息
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-27
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ShmtuBook {

	// 书名
	private String title;
	// 链接地址
	private String link;
	// 作者
	private String author;
	// 出版时间
	private String pubDate;
	// 描述
	private String description;

	public ShmtuBook(String title, String link, String author, String pubDate,
			String description) {
		super();
		this.title = title;
		this.link = link;
		this.author = author;
		this.pubDate = pubDate;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

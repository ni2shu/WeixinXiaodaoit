package co.xiaodao.weixin.json.face;

import java.util.List;

/**
 * 获取图片中的所有人脸
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class FaceJson {
	// 相应请求的session标识符，可用于结果查询
	private String session_id;
	// 请求中图片的url
	private String url;
	// Face++系统中的图片标识符，用于标识用户请求中的图片
	private String img_id;
	// 请求图片的宽度
	private String img_width;
	// 请求图片的高度
	private String img_height;
	// 被检测出的人脸的列表
	private List<FaceSingle> face;

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg_id() {
		return img_id;
	}

	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}

	public String getImg_width() {
		return img_width;
	}

	public void setImg_width(String img_width) {
		this.img_width = img_width;
	}

	public String getImg_height() {
		return img_height;
	}

	public void setImg_height(String img_height) {
		this.img_height = img_height;
	}

	public List<FaceSingle> getFace() {
		return face;
	}

	public void setFace(List<FaceSingle> face) {
		this.face = face;
	}

}

package co.xiaodao.weixin.json.face;

/**
 * 单个人脸
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class FaceSingle implements Comparable<FaceSingle> {
	// 人脸标记
	private String tag;
	// 人脸ID
	private String face_id;
	// 包含一系列人脸的属性分析结果
	private FaceAttribute attribute;
	// 五官位置
	private FacePosition position;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getFace_id() {
		return face_id;
	}

	public void setFace_id(String face_id) {
		this.face_id = face_id;
	}

	public FaceAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(FaceAttribute attribute) {
		this.attribute = attribute;
	}

	public FacePosition getPosition() {
		return position;
	}

	public void setPosition(FacePosition position) {
		this.position = position;
	}

	@Override
	public int compareTo(FaceSingle faceSingle) {
		int result = 0;
		if (this.position.getCenter().getX() > faceSingle.getPosition()
				.getCenter().getX()) {
			result = 1;
		} else {
			result = -1;
		}
		return result;
	}

}

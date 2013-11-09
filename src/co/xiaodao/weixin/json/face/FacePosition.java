package co.xiaodao.weixin.json.face;

/**
 * 器官的位置集合
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class FacePosition {
	// 检出的人脸框的中心点坐标, x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
	private FacePositionSub center;
	// 相应人脸的左眼坐标，x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
	private FacePositionSub eye_left;
	// 相应人脸的右眼坐标，x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
	private FacePositionSub eye_right;
	// 相应人脸的左侧嘴角坐标，x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
	private FacePositionSub mouth_left;
	// 相应人脸的右侧嘴角坐标，x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
	private FacePositionSub mouth_right;
	// 相应人脸的鼻尖坐标，x & y 坐标分别表示在图片中的宽度和高度的百分比 (0~100之间的实数)
	private FacePositionSub nose;
	// 0~100之间的实数，表示检出的脸的高度在图片中百分比
	private String height;
	// 0~100之间的实数，表示检出的脸的宽度在图片中百分比
	private String width;

	public FacePositionSub getCenter() {
		return center;
	}

	public void setCenter(FacePositionSub center) {
		this.center = center;
	}

	public FacePositionSub getEye_left() {
		return eye_left;
	}

	public void setEye_left(FacePositionSub eye_left) {
		this.eye_left = eye_left;
	}

	public FacePositionSub getEye_right() {
		return eye_right;
	}

	public void setEye_right(FacePositionSub eye_right) {
		this.eye_right = eye_right;
	}

	public FacePositionSub getMouth_left() {
		return mouth_left;
	}

	public void setMouth_left(FacePositionSub mouth_left) {
		this.mouth_left = mouth_left;
	}

	public FacePositionSub getMouth_right() {
		return mouth_right;
	}

	public void setMouth_right(FacePositionSub mouth_right) {
		this.mouth_right = mouth_right;
	}

	public FacePositionSub getNose() {
		return nose;
	}

	public void setNose(FacePositionSub nose) {
		this.nose = nose;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

}

package co.xiaodao.weixin.json.face;

/**
 * 包含性别分析结果
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class FaceAttributeGender {
	// value的值为Male/Female
	private String value;
	// confidence表示置信度
	private String confidence;

	public static String getFaceAttributeGender(String value, String confidence) {
		if (confidence.length() >= 4) {
			confidence = confidence.substring(0, 4);
		}
		if (value.equals("Male")) {
			value = "男性";
		} else {
			value = "女性";
		}
		String returnStr = "  性别：" + confidence + "%是" + value;
		return returnStr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

}

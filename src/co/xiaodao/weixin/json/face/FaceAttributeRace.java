package co.xiaodao.weixin.json.face;

/**
 * 包含人种分析结果
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class FaceAttributeRace {
	// value的值为Asian/White/Black
	private String value;
	// confidence表示置信度
	private String confidence;

	public static String getFaceAttributeRace(String value, String confidence) {
		if (confidence.length() >= 4) {
			confidence = confidence.substring(0, 4);
		}
		if (value.equals("Asian")) {
			value = "黄种人";
		} else if (value.equals("White")) {
			value = "白种人";
		} else {
			value = "黑种人";
		}
		String returnStr = "  人种：" + confidence + "%是" + value;
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

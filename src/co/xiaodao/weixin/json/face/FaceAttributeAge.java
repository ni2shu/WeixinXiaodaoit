package co.xiaodao.weixin.json.face;

/**
 * 包含年龄分析结果
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class FaceAttributeAge {
	// value的值为一个非负整数表示估计的年龄
	private String value;
	// range表示估计年龄的正负区间
	private String range;

	public static String getFaceAttributeAge(String value, String range) {
		String returnStr = "  年龄：" + value + "±" + range + "岁";
		return returnStr;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

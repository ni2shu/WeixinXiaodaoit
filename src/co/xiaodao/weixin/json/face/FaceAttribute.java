package co.xiaodao.weixin.json.face;

/**
 * 包含一系列人脸的属性分析结果
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class FaceAttribute {
	// 包含年龄分析结果，value的值为一个非负整数表示估计的年龄, range表示估计年龄的正负区间
	private FaceAttributeAge age;
	// 包含性别分析结果，value的值为Male/Female, confidence表示置信度
	private FaceAttributeGender gender;
	// 包含人种分析结果，value的值为Asian/White/Black, confidence表示置信度
	private FaceAttributeRace race;

	public static String getFaceAttribute(FaceAttributeAge age,
			FaceAttributeGender gender, FaceAttributeRace race) {
		String returnStr = "";
		returnStr += FaceAttributeAge.getFaceAttributeAge(age.getValue(),
				age.getRange())
				+ "\n";
		returnStr += FaceAttributeGender.getFaceAttributeGender(
				gender.getValue(), gender.getConfidence())
				+ "\n";
		returnStr += FaceAttributeRace.getFaceAttributeRace(race.getValue(),
				race.getConfidence());
		return returnStr;
	}

	public FaceAttributeAge getAge() {
		return age;
	}

	public void setAge(FaceAttributeAge age) {
		this.age = age;
	}

	public FaceAttributeGender getGender() {
		return gender;
	}

	public void setGender(FaceAttributeGender gender) {
		this.gender = gender;
	}

	public FaceAttributeRace getRace() {
		return race;
	}

	public void setRace(FaceAttributeRace race) {
		this.race = race;
	}

}

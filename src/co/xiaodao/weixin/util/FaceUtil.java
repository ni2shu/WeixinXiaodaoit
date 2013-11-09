package co.xiaodao.weixin.util;

import co.xiaodao.weixin.json.face.FaceJson;

import com.google.gson.Gson;

/**
 * 人脸检测工具类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class FaceUtil {

	public static final String FACE_API_KEY = "bf5b66b7ca2df13106b1969702ec3ad5";
	public static final String FACE_API_SECRET = "tc5AQh-PlFp7iP3XYCw_s6IZ6uQ-pj1x";
	public static final String FACE_URL_PRE = "https://apicn.faceplusplus.com/v2/detection/detect?api_secret="
			+ FACE_API_SECRET + "&api_key=" + FACE_API_KEY + "&url=";

	public static FaceJson jsonToFace(String json) {
		Gson gson = new Gson();
		FaceJson faceJson = gson.fromJson(json, FaceJson.class);
		return faceJson;
	}

	public static void main(String[] args) {
		System.out.println();
	}

}

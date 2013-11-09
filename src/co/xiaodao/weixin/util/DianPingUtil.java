package co.xiaodao.weixin.util;

import java.util.HashMap;
import java.util.Map;

import co.xiaodao.weixin.json.dianping.DianPingJson;
import co.xiaodao.weixin.service.DianPingService;

import com.google.gson.Gson;

/**
 * 大众点评工具类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class DianPingUtil {

	public static String DIAN_PING_API_URL = "http://api.dianping.com/v1/business/find_businesses";
	public static String DIAN_PING_API_KEY = "446749641";
	public static String DIAN_PING_SECRET = "8ac305dbd2074c8ab29e94f613dc1b8a";
	//public static String MY_DOMAIN = "cdztop.eicp.net";
	// public static String MY_DOMAIN = "xiaodaoit.gotoip2.com";

	/**
	 * 将json格式的商家信息转化成java对象
	 * 
	 * @param json
	 * @return
	 */
	public static DianPingJson jsonToDianPing(String json) {
		Gson gson = new Gson();
		DianPingJson dianPingJson = gson.fromJson(json, DianPingJson.class);
		return dianPingJson;
	}

	/**
	 * 请求参数设置
	 * 
	 * @param latitude
	 * @param longitude
	 * @param category
	 * @return
	 */
	public static Map<String, String> getParamMap(String latitude,
			String longitude, String category) {

		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("format", "json");// 返回数据格式，可选值为json或xml，如不传入，默认值为json
		paramMap.put("latitude", latitude);// 纬度坐标，须与经度坐标同时传入，与城市名称二者必选其一传入
		paramMap.put("longitude", longitude);// 经度坐标，须与纬度坐标同时传入，与城市名称二者必选其一传入
		paramMap.put("category",
				(category != null && !"".equals(category)) ? category : "美食");// 分类名，可选范围见相关API返回结果
		paramMap.put("limit", "9");// 返回的商户结果条目数上限，最小值1，最大值20，如不传入默认为20
		paramMap.put("radius", "2000");// 搜索半径，单位为米，最小值1，最大值5000，如不传入默认为1000
		// paramMap.put("offset_type", "0");//
		// 偏移类型，0:未偏移，1:高德坐标系偏移，2:四维图新坐标系偏移，如不传入，默认值为0
		// paramMap.put("has_coupon", "1");// 根据是否有优惠券来筛选返回的商户，1:有，0:没有
		// paramMap.put("has_deal", "1");// 根据是否有团购来筛选返回的商户，1:有，0:没有
		// paramMap.put("keyword", "泰国菜");// 关键词，搜索范围包括商户名、地址、标签等
		paramMap.put("sort", "7");// 结果排序，1:默认，2:星级高优先，3:产品评价高优先，4:环境评价高优先，5:服务评价高优先，6:点评数量多优先，7:离传入经纬度坐标距离近优先
		paramMap.put("platform", "2");// 传出链接类型，1:web站链接（适用于网页应用），2:wap站链接（适用于移动应用和联网车载应用），如不传入，默认值为1

		return paramMap;
	}

	public static void main(String[] args) {
		DianPingJson dianPingJson = DianPingService.requestApi(
				DIAN_PING_API_URL, DIAN_PING_API_KEY, DIAN_PING_SECRET,
				getParamMap("31.118500", "121.494035", ""));

		System.out.println(dianPingJson.getStatus());
		System.out.println(dianPingJson.getCount());
		System.out.println(dianPingJson.getBusinesses().get(0).getAddress());

	}

}

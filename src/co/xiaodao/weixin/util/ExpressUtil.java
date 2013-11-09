package co.xiaodao.weixin.util;

import co.xiaodao.weixin.json.express.ExpressJson;

import com.google.gson.Gson;

/**
 * 快递查询查询工具类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ExpressUtil {

	public final static String URL_EXPRESS = "http://www.aikuaidi.cn/rest/?key=3a9d4f627d5b42509b119ba3b9a1576e&order={order_num}&id={express_id}";

	/**
	 * errCode 返回错误码： 0：无错误， 1：快递KEY无效， 2：快递代号无效， 3：访问次数达到最大额度，
	 * 4：查询服务器返回错误即返回状态码非200, 5：程序执行出错
	 */
	public final static String express_errCode_msg_0 = "无错误";
	public final static String express_errCode_msg_1 = "快递KEY无效";
	public final static String express_errCode_msg_2 = "快递代号无效";
	public final static String express_errCode_msg_3 = "访问次数达到最大额度";
	public final static String express_errCode_msg_4 = "查询服务器返回错误即返回状态码非200";
	public final static String express_errCode_msg_5 = "程序执行出错";

	/**
	 * status 订单跟踪状态： 0：查询出错（即errCode!=0）， 1：暂无记录， 2：在途中， 3：派送中， 4：已签收， 5：拒收，
	 * 6：疑难件 7：退回
	 */
	public final static String express_status_msg_0 = "查询出错（即errCode!=0）";
	public final static String express_status_msg_1 = "暂无记录";
	public final static String express_status_msg_2 = "在途中";
	public final static String express_status_msg_3 = "派送中";
	public final static String express_status_msg_4 = "已签收";
	public final static String express_status_msg_5 = "拒收";
	public final static String express_status_msg_6 = "疑难件";
	public final static String express_status_msg_7 = "退回";
	public final static String express_status_msg_default = "未知";

	public static String expressStatusFromCodeToString(String statusCodeStr) {
		int statusCode = Integer.parseInt(statusCodeStr);
		String returnStr = "";
		switch (statusCode) {
		case 0:
			returnStr = express_status_msg_0;
			break;
		case 1:
			returnStr = express_status_msg_1;
			break;
		case 2:
			returnStr = express_status_msg_2;
			break;
		case 3:
			returnStr = express_status_msg_3;
			break;
		case 4:
			returnStr = express_status_msg_4;
			break;
		case 5:
			returnStr = express_status_msg_5;
			break;
		case 6:
			returnStr = express_status_msg_6;
			break;
		case 7:
			returnStr = express_status_msg_7;
			break;
		default:
			returnStr = express_status_msg_default;
			break;
		}
		return returnStr;
	}

	public static ExpressJson jsonToExpress(String json) {
		Gson gson = new Gson();
		ExpressJson expressJson = gson.fromJson(json, ExpressJson.class);
		return expressJson;
	}

	public static void main(String[] args) {
		System.out.println(URL_EXPRESS.replace("{order_num}", "3147403986")
				.replace("{express_id}", "yuantong"));
	}

}

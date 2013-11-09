package co.xiaodao.weixin.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.xiaodao.weixin.db.pojo.MsgCache;
import co.xiaodao.weixin.db.util.MsgCacheDBUtil;
import co.xiaodao.weixin.json.express.ExpressJson;
import co.xiaodao.weixin.json.express.ExpressSingleData;
import co.xiaodao.weixin.util.ExpressUtil;
import co.xiaodao.weixin.util.XiaoDaoUtil;

/**
 * 快递查询的服务
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
@SuppressWarnings("unchecked")
public class ExpressService {
	private static Logger log = LoggerFactory.getLogger(ExpressService.class);

	// classpath
	public static final String classpath = ExpressService.class
			.getResource("/").getPath().replaceAll("%20", " ");
	// 快递代码配置文件
	public static String PATH_CONF = classpath + "express-conf.xml";

	public static Map<String, String> expressMap = new HashMap<String, String>();

	// 静态代码块加载快递代码配置信息
	static {
		try {
			// 通过SAX解析输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(PATH_CONF));
			// 得到xml根元素
			Element root = document.getRootElement();
			// 得到root子节点集合
			List<Element> urlList = root.elements("express");
			for (Element e : urlList)
				expressMap
						.put(e.attributeValue("name"), e.attributeValue("id"));
		} catch (Exception e) {
			log.error("{}", e);
		}
	}

	public static String queryExpress(String order_num, String expressName, String openId) {
		String express_id = findExpressCodeByName(expressName);
		StringBuffer returnStrBuffer = new StringBuffer();
		returnStrBuffer.append("快递公司：" + expressName + "\n");
		returnStrBuffer.append("快递单号：" + order_num + "\n");
		if (express_id == null) {
			returnStrBuffer.append("没有查询到快递公司，请核对信息后再查询。");
		} else {
			ExpressJson expressJson = null;
			String requestUrl = ExpressUtil.URL_EXPRESS.replace("{order_num}",
					order_num).replace("{express_id}", express_id);
			String returnJsonStr = XiaoDaoUtil.getJsonByHttp(requestUrl,
					"gb2312");
		//	System.out.println(returnJsonStr);
			expressJson = ExpressUtil.jsonToExpress(returnJsonStr);
			if (expressJson == null) {
				returnStrBuffer.append("没有查询到物流信息，请核对信息后再查询。");
			} else {
				if (expressJson.getErrCode().equals("0")) {
					// 0：无错误，
					returnStrBuffer.append("快递状态：" + ExpressUtil.expressStatusFromCodeToString(expressJson.getStatus()) + "\n");
					if (!expressJson.getStatus().equals("1")) {
						returnStrBuffer.append("\n快递跟踪：\n");
						List<ExpressSingleData> expressData = expressJson.getData();
						for (ExpressSingleData expressSingleData : expressData) {
							returnStrBuffer.append(expressSingleData.getTime() + "\n");
							returnStrBuffer.append(expressSingleData.getContent() + "\n");
						}
					}
					MsgCache msgCache = MsgCacheDBUtil.getMsgCacheByOpenID(openId);
					if(msgCache != null){
						msgCache.setExpress(order_num + "@" + expressName);
						MsgCacheDBUtil.updateMsgCache(msgCache);
					}else{
						MsgCacheDBUtil.insertMsgCache(new MsgCache(openId,"",order_num + "@" + expressName));
					}
				} else if (expressJson.getErrCode().equals("2")) {
					// 2：快递代号无效，
					returnStrBuffer.append("快递公司不正确，请核对信息后再查询。");
				} else {
					returnStrBuffer.append("服务器端出问题了，请反馈给开发者微信号『cdztop』");
				}
			}
		}
		returnStrBuffer.append("\n\n-------------------\n如要查询其他快递\n\n发送：快递+单号@公司\n例如发送：\n快递3147403986@圆通");
		return returnStrBuffer.toString();
	}

	public static String findExpressCodeByName(String expressName) {
		String expressId = null;

		if (expressMap.containsKey(expressName))
			expressId = expressMap.get(expressName);
		else if (expressMap.containsKey(expressName + "快递"))
			expressId = expressMap.get(expressName + "快递");
		else if (expressMap.containsKey(expressName + "物流"))
			expressId = expressMap.get(expressName + "物流");
		else if (expressMap.containsKey(expressName + "快运"))
			expressId = expressMap.get(expressName + "快运");

		return expressId;
	}

	public static void main(String[] args) {
	}
}

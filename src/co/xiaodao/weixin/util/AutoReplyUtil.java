package co.xiaodao.weixin.util;

import java.util.Random;

/**
 * 智能回复工具类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class AutoReplyUtil {
	// classpath
	public static final String classpath = AutoReplyUtil.class.getResource("/")
			.getPath().replaceAll("%20", " ");
	// 索引存储位置
	public static String PATH_INDEX = classpath + "index/";

	// 主键标识
	public static String FIELD_ID = "id";
	// 问题
	public static String FIELD_QUESTION = "q";
	// 答案
	public static String FIELD_ANSWER = "a";
	// 分类
	public static String FIELD_CATEGORY = "c";

	/**
	 * 随机生成 0~length-1 之间的某个值
	 * 
	 * @return int
	 */
	public static int getRandomNumber(int length) {
		Random random = new Random();
		return random.nextInt(length);
	}

	public static void main(String[] args) {
		// System.out.println(AutoReplyService.search("周星驰"));
		//System.out.println(getRandomNumberForFileName());
	}
}

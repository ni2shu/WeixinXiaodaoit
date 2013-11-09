package co.xiaodao.weixin.util;

/**
 * 百度音乐搜索接口工具类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class BaiduMusicUtil {

	// 百度音乐搜索的地址<br>
	// 例如：http://box.zhangmen.baidu.com/x?op=12&count=1&title=同桌的你$$$$$$
	public final static String URL = "http://box.zhangmen.baidu.com/x?op=12&count=1&title={music_title}$${music_author}$$$$";

	// 百度音乐搜索结果xml中表示记录数的元素
	public final static String ELEMENT_COUNT = "count";

	// 百度音乐搜索结果xml中表示普通音质的元素
	public final static String ELEMENT_URL = "url";

	// 百度音乐搜索结果xml中表示高级音质的元素
	public final static String ELEMENT_DURL = "durl";

	// 百度音乐搜索结果xml中表示编码后的url元素
	public final static String ELEMENT_ENCODE = "encode";

	// 百度音乐搜索结果xml中表示编码前的url元素
	public final static String ELEMENT_DECODE = "decode";
}

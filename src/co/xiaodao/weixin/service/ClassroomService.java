package co.xiaodao.weixin.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 找教室的服务
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
public class ClassroomService {
	private static Logger log = LoggerFactory.getLogger(ClassroomService.class);

	// classpath
	public static final String classpath = ClassroomService.class
			.getResource("/").getPath().replaceAll("%20", " ");
	// 所有教室配置文件
	public static String PATH_CONF = classpath + "classroom-conf.xml";

	public static List<String> classroomListAll = new ArrayList<String>();

	// 静态代码块加载教室代码配置信息
	static {
		try {
			// 通过SAX解析输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(PATH_CONF));
			// 得到xml根元素
			Element root = document.getRootElement();
			// 得到root子节点集合
			List<Element> urlList = root.elements("classroom");
			for (Element e : urlList) {
				classroomListAll.add(e.attributeValue("name"));
			}
		} catch (Exception e) {
			log.error("{}", e);
		}
	}

	/**
	 * 判断大节号字符是否是124567
	 * 
	 * @param ch
	 * @return
	 */
	public static boolean isTrueNum(char ch) {
		if (ch == '1' || ch == '2' || ch == '4' || ch == '5' || ch == '6' || ch == '7') {
			return true;
		}
		return false;
	}
//	public static boolean isTrueNum(char ch) {
//		if (ch == '1' || ch == '2' || ch == '3' || ch == '4') {
//			return true;
//		}
//		return false;
//	}

	/**
	 * 判断大节号是否符合要求
	 * 
	 * @param num
	 * @return
	 */
//	public static boolean isTrueNumFormat(String num) {
//		if (num.length() > 4) {
//			return false;
//		} else {
//			char[] strArr = num.toCharArray();
//			for (int i = 0; i < strArr.length; i++) {
//				if (!isTrueNum(strArr[i])) {
//					return false;
//				}
//			}
//		}
//		return true;
//	}
	public static boolean isTrueNumFormat(String num) {
		if (num.length() > 6) {
			return false;
		} else {
			char[] strArr = num.toCharArray();
			for (int i = 0; i < strArr.length; i++) {
				if (!isTrueNum(strArr[i])) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 根据时间获取当前的周次
	 * 
	 * @param time
	 * @return
	 */
	public static String getWeekly(String time) {
		String weekNum = null;
		if (time.compareTo("2013-09-02") >= 0 && time.compareTo("2013-09-08") <= 0) {
			weekNum = "1";
		} else if (time.compareTo("2013-09-09") >= 0 && time.compareTo("2013-09-15") <= 0) {
			weekNum = "2";
		} else if (time.compareTo("2013-09-16") >= 0 && time.compareTo("2013-09-22") <= 0) {
			weekNum = "3";
		} else if (time.compareTo("2013-09-23") >= 0 && time.compareTo("2013-09-29") <= 0) {
			weekNum = "4";
		} else if (time.compareTo("2013-09-30") >= 0 && time.compareTo("2013-10-06") <= 0) {
			weekNum = "5";
		} else if (time.compareTo("2013-10-07") >= 0 && time.compareTo("2013-10-13") <= 0) {
			weekNum = "6";
		} else if (time.compareTo("2013-10-14") >= 0 && time.compareTo("2013-10-20") <= 0) {
			weekNum = "7";
		} else if (time.compareTo("2013-10-21") >= 0 && time.compareTo("2013-10-27") <= 0) {
			weekNum = "8";
		} else if (time.compareTo("2013-10-28") >= 0 && time.compareTo("2013-11-03") <= 0) {
			weekNum = "9";
		} else if (time.compareTo("2013-11-04") >= 0 && time.compareTo("2013-11-10") <= 0) {
			weekNum = "10";
		} else if (time.compareTo("2013-11-11") >= 0 && time.compareTo("2013-11-17") <= 0) {
			weekNum = "11";
		} else if (time.compareTo("2013-11-18") >= 0 && time.compareTo("2013-11-24") <= 0) {
			weekNum = "12";
		} else if (time.compareTo("2013-11-25") >= 0 && time.compareTo("2013-12-01") <= 0) {
			weekNum = "13";
		} else if (time.compareTo("2013-12-02") >= 0 && time.compareTo("2013-12-08") <= 0) {
			weekNum = "14";
		} else if (time.compareTo("2013-12-09") >= 0 && time.compareTo("2013-12-15") <= 0) {
			weekNum = "15";
		} else if (time.compareTo("2013-12-16") >= 0 && time.compareTo("2013-12-22") <= 0) {
			weekNum = "16";
		} else if (time.compareTo("2013-12-23") >= 0 && time.compareTo("2013-12-29") <= 0) {
			weekNum = "17";
		} else if (time.compareTo("2013-12-30") >= 0 && time.compareTo("2014-01-05") <= 0) {
			weekNum = "18";
		} else if (time.compareTo("2014-01-06") >= 0 && time.compareTo("2014-01-12") <= 0) {
			weekNum = "19";
		} else if (time.compareTo("2014-01-13") >= 0 && time.compareTo("2014-01-19") <= 0) {
			weekNum = "20";
		}
		return weekNum;
	}

	/**
	 * 获取当前的星期
	 * 
	 * @param date
	 * @return
	 */
	public static String getXinQI(Date date) {
		String xinQiNum = null;
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		xinQiNum = dateFm.format(date);
		String returnStr = null;

		if (xinQiNum.equals("星期一")) {
			returnStr = "1";
		} else if (xinQiNum.equals("星期二")) {
			returnStr = "2";
		} else if (xinQiNum.equals("星期三")) {
			returnStr = "3";
		} else if (xinQiNum.equals("星期四")) {
			returnStr = "4";
		} else if (xinQiNum.equals("星期五")) {
			returnStr = "5";
		} else if (xinQiNum.equals("星期六")) {
			returnStr = "6";
		} else if (xinQiNum.equals("星期日")) {
			returnStr = "7";
		}
		return returnStr;
	}

	/**
	 * 根据当前时间获取大节号 如果返回null:表示不再上课时间
	 */
	public static String getJieChi(Date date) {
		String nowTime = null;
		SimpleDateFormat dateFm = new SimpleDateFormat("HH:mm");
		nowTime = dateFm.format(date);
		String returnStr = null;

		if (nowTime.compareTo("08:20") >= 0 && nowTime.compareTo("09:55") <= 0) {
			returnStr = "1";
		} else if (nowTime.compareTo("10:15") >= 0
				&& nowTime.compareTo("11:50") <= 0) {
			returnStr = "2";
		} else if (nowTime.compareTo("13:10") >= 0
				&& nowTime.compareTo("14:45") <= 0) {
			returnStr = "4";
		} else if (nowTime.compareTo("15:05") >= 0
				&& nowTime.compareTo("16:40") <= 0) {
			returnStr = "5";
		} else if (nowTime.compareTo("18:00") >= 0
				&& nowTime.compareTo("19:35") <= 0) {
			returnStr = "6";
		} else if (nowTime.compareTo("19:40") >= 0
				&& nowTime.compareTo("20:25") <= 0) {
			returnStr = "7";
		}
		return returnStr;
	}
	
//	public static String getJieChi(Date date) {
//		String nowTime = null;
//		SimpleDateFormat dateFm = new SimpleDateFormat("HH:mm");
//		nowTime = dateFm.format(date);
//		String returnStr = null;
//
//		if (nowTime.compareTo("08:20") >= 0 && nowTime.compareTo("10:05") <= 0) {
//			returnStr = "1";
//		} else if (nowTime.compareTo("10:25") >= 0 && nowTime.compareTo("12:10") <= 0) {
//			returnStr = "2";
//		} else if (nowTime.compareTo("13:10") >= 0 && nowTime.compareTo("14:55") <= 0) {
//			returnStr = "3";
//		} else if (nowTime.compareTo("15:15") >= 0 && nowTime.compareTo("17:00") <= 0) {
//			returnStr = "4";
//		} 
//		return returnStr;
//	}

	/**
	 * 该课程在本周是否有课
	 * 
	 * @param weekly
	 * @param weeklyStr
	 * @return
	 */
	public static boolean haveWeekly(String weekly, String weeklyStr) {
		String[] weeklyArr = weeklyStr.split("-");
		for (int i = 0; i < weeklyArr.length; i++) {
			if (weekly.equals(weeklyArr[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 去掉上课的教室
	 * 
	 * @param listAll
	 * @param listSelect
	 * @return
	 */
	public static List<String> deleteInUseClassroom(List<String> listAll,
			List<String> listSelect) {
		if (listAll.removeAll(listSelect)) {
			return listAll;
		}
		return null;
	}

	public static String getClassroomStr(List<String> listNoUse) {
		String returnStr = "";
		int i = 0;
		if (listNoUse != null && listNoUse.size() > 0) {
			for (String str : listNoUse) {
				returnStr += str + "  ";
				i++;
				if (i == 3) {
					returnStr += "\n";
					i = 0;
				}
			}
		} else {
			returnStr = null;
		}
		return returnStr;
	}

	public static void main(String[] args) {
		List<String> list0 = new ArrayList<String>();
		list0.add("1");
		list0.add("2");
		list0.add("3");
		list0.add("4");
		list0.add("5");
		list0.add("6");
		list0.add("7");
		list0.add("8");
		list0.add("9");
		list0.add("10");
		
		List<String> list1 = new ArrayList<String>();
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list1.add("4");
		list1.add("5");
		List<String> list2 = new ArrayList<String>();
		list2.add("5");
		List<String> list3 = new ArrayList<String>();
		list2.add("2");
		list2.add("9");

//		ClassroomService.deleteInUseClassroom(list1, list2);
//		ClassroomService.deleteInUseClassroom(list1, list3);
		list1.addAll(list2);
		list1.addAll(list3);
		list0.removeAll(list1);
		System.out.println(getClassroomStr(list0));

		// List<String> strList = ClassroomDBUtil.getInUseClassroom("11", "4");
		// System.out.println(deleteInUseClassroom(classroomListAll, strList));
		// System.out.println(haveWeekly("9", "1-2-3-4--7-8-9-10-12-"));
	}

}

package co.xiaodao.weixin.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;

import org.apache.commons.httpclient.methods.GetMethod;

import org.apache.commons.httpclient.params.HttpMethodParams;

import co.xiaodao.weixin.db.pojo.User;
import co.xiaodao.weixin.db.util.BaseDBUtil;
import co.xiaodao.weixin.db.util.UserDBUtil;
import co.xiaodao.weixin.message.pojo.ClassSchedule;
import co.xiaodao.weixin.message.pojo.NewScore;
import co.xiaodao.weixin.message.pojo.PassScore;
import co.xiaodao.weixin.util.WeixinUtil;

public class ShmtuAuthService {

	// 最新成绩
	public static String latest_score = "http://jwc.shmtu.edu.cn:7778/pls/wwwbks/bkscjcx.curscopre?jym2005=11875.214065059998";
	// 学籍信息
	// public static String student_status =
	// "http://jwc.shmtu.edu.cn:7778/pls/wwwbks/bks_xj.xjcx?jym2005=6187.330176809112";
	// 课表
	public static String class_schedule = "http://jwc.shmtu.edu.cn:7778/pls/wwwbks/xk.CourseView?jym2005=12696.010737745353";
	// 通过成绩
	public static String pass_score = "http://jwc.shmtu.edu.cn:7778/pls/wwwbks/bkscjcx.yxkc?jym2005=12179.703070423186";
	// 最新成绩
	public static String new_score = "http://jwc.shmtu.edu.cn:7778/pls/wwwbks/bkscjcx.curscopre?jym2005=11875.214065059998";

	// 验证接口
	public static String auth_url = "http://jwc.shmtu.edu.cn:7778/pls/wwwbks/bks_login2.login?stuid={stuid}&pwd={pwd}";

	public static String auth_url_short = "http://jwc.shmtu.edu.cn:7778/pls/wwwbks/bks_login2.login";

	// 网页绑定的URL
	public static String auth_url_bind_pre = "http://" + BaseDBUtil.WEB_HOSE
			+ "/WeixinXiaodaoit/bind/auth.jsp?oauth=";

	// public static String auth_url_pass_cdz = auth_url.replace("{stuid}",
	// "200810314021").replace("{pwd}", "woaishmtu");
	// public static String auth_url_pass_zjl = auth_url.replace("{stuid}",
	// "200910314077").replace("{pwd}", "zjl5967786");

	// 通过验证之后的重定向
	public static String pass_auth_redirect = "http://jwc.shmtu.edu.cn:7778/pls/wwwbks/bks_login2.loginmessage";

	public static void main(String[] args) throws IOException {
		// System.out.println(getContentAfterAuthByHttpGet("200810314021",
		// "123",
		// pass_score));
		//String[] str = getPassScore("ofY_njiagXqwZCwbjFLoaaXV4erc");
		String[] str = getNewScore("ofY_njl1Sy8YbE7iJfj7k3SfOI80");
		System.out.println(str[1]);

	}
	
	//获取最新成绩
	public static String[] getNewScore(String openID) {
		String strAll[] = goToBind(openID, new_score);
		if (!strAll[0].equals("0")) {
			Pattern p0 = Pattern.compile("(.*)(备注</p></td>)(.*)(VALUE=\"显示排名\")(.*)");
			Matcher m0 = p0.matcher(strAll[1]);
			
			Pattern p3 = Pattern.compile("(.*)(<td width=\"112\" height=\"20\" class=td_biaogexian><p align=\"center\">)(.*)(</p>)(.*)");
			Pattern p4 = Pattern.compile("(.*)(<td width=\"200\" height=\"20\" class=td_biaogexian><p align=\"center\">)(.*)(</p>)(.*)");
			Matcher m3 = null;
			Matcher m4 = null;
			int i = 0;
			String strTemp = "";
			List<NewScore> listNewScore = new ArrayList<NewScore>();
			
			if (m0.matches()) {
				for (String info : m0.group(3).split("<TR>")) {
					i = 0;
					NewScore newScore = new NewScore();
					for (String info2 : info.split("</td>")) {
						i++;
						m3 = p3.matcher(info2);
						m4 = p4.matcher(info2);
						if (m3.matches()) {
							strTemp = m3.group(3).replaceAll("&nbsp;", " ").trim();
							//System.out.println(m3.group(3));
						} else if (m4.matches()) {
							strTemp = m4.group(3).replaceAll("&nbsp;", " ").trim();
							//System.out.println(m4.group(3));
						}
						switch (i) {
						case 3:
							newScore.setClass_name(strTemp);
							break;
						case 5:
							newScore.setXf(strTemp);
							break;
						case 7:
							newScore.setScore(strTemp);
							break;
						}
					}
					listNewScore.add(newScore);
				}
			}
			String returnStr = "";
			if(listNewScore.size() != 0){
				listNewScore.remove(0);
				for (NewScore n : listNewScore) {
					returnStr += n.getClass_name()  + "\n学分:" + n.getXf() + "\n成绩:" + n.getScore() + "\n\n";
				}
			}else{
				returnStr = "你本学期没有课程。";
			}
			strAll[1] = returnStr;
			//System.out.println(returnStr);
		}	
		return strAll;
	}
	
	// 获取通过课程
	public static String[] getPassScore(String openID) {
		String strAll[] = goToBind(openID, pass_score);
		if (!strAll[0].equals("0")) {

			String xfTotal = "";
			Pattern p0 = Pattern
					.compile("(.*)(<B>学位绩点:</B>)(.*)(<font color=red>)(.*)(</font>)(.*)(<B>    统计时间:</B>)(.*)(<font color=red>)(.*)(</font>)(.*)(<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=table_biankuan>)(.*)(教学计划内（课程号一致）通过课程成绩表)(.*)");
			Matcher m0 = p0.matcher(strAll[1]);
			if (m0.matches() && !m0.group(5).trim().equals("null")) {
				xfTotal = "学位绩点:" + m0.group(5).trim() + "\n统计时间:"
						+ m0.group(11).trim() + "\n\n";
			}

			String strAll2 = null;
			Pattern p1 = Pattern
					.compile("(.*)(教学计划内（课程号一致）通过课程成绩表)(.*)(教学计划外（课程号不一致）---通过课程成绩表)(.*)");
			Matcher m1 = p1.matcher(strAll[1]);
			if (m1.matches()) {
				strAll2 = m1.group(3);
			}
			Pattern p2 = Pattern
					.compile("(.*)(<td  height=\"25\"  class=td_hz_bj><p align=\"center\">备注</p></td>)(.*)(<B>   累计学分：</B>)(.*)");
			Matcher m2 = p2.matcher(strAll2);
			Pattern p3 = Pattern
					.compile("(.*)(<td width=\"112\" height=\"20\" class=td_biaogexian><p align=\"center\">)(.*)(</p>)(.*)");
			Pattern p4 = Pattern
					.compile("(.*)(<td width=\"200\" height=\"20\" class=td_biaogexian><p align=\"center\">)(.*)(</p>)(.*)");
			Matcher m3 = null;
			Matcher m4 = null;
			int i = 0;
			String strTemp = "";
			List<PassScore> listPassScore = new ArrayList<PassScore>();
			if (m2.matches()) {
				for (String info : m2.group(3).split("<TR>")) {
					i = 0;
					PassScore passScore = new PassScore();
					for (String info2 : info.split("</td>")) {
						i++;
						m3 = p3.matcher(info2);
						m4 = p4.matcher(info2);
						if (m3.matches()) {
							strTemp = m3.group(3).replaceAll("&nbsp;", " ")
									.trim();
							// System.out.println(m3.group(3));
						} else if (m4.matches()) {
							strTemp = m4.group(3).replaceAll("&nbsp;", " ")
									.trim();
							// System.out.println(m4.group(3));
						}
						switch (i) {
						case 1:
							passScore.setClass_num(strTemp);
							break;
						case 2:
							passScore.setClass_name(strTemp);
							break;
						case 3:
							passScore.setNum(strTemp);
							break;
						case 4:
							passScore.setXf(strTemp);
							break;
						case 5:
							passScore.setKssj(strTemp);
							break;
						case 6:
							passScore.setScore(strTemp);
							break;
						case 7:
							passScore.setCh_class_num(strTemp);
							break;
						case 8:
							passScore.setRemark(strTemp);
							break;
						}
					}
					listPassScore.add(passScore);
				}
			}
			listPassScore.remove(0);

			p1 = Pattern
					.compile("(.*)(教学计划外（课程号不一致）---通过课程成绩表)(.*)(课组计划内已通过课程成绩表)(.*)");
			m1 = p1.matcher(strAll[1]);
			if (m1.matches()) {
				strAll2 = m1.group(3);
			}
			p2 = Pattern
					.compile("(.*)(<td  height=\"25\"  class=td_hz_bj><p align=\"center\">备注</p></td>)(.*)(<B>   累计学分：：</B>)(.*)");
			m2 = p2.matcher(strAll2);
			p3 = Pattern
					.compile("(.*)(<td width=\"112\" height=\"20\" class=td_biaogexian><p align=\"center\">)(.*)(</p>)(.*)");
			p4 = Pattern
					.compile("(.*)(<td width=\"200\" height=\"20\" class=td_biaogexian><p align=\"center\">)(.*)(</p>)(.*)");
			m3 = null;
			m4 = null;

			List<PassScore> listPassScore2 = new ArrayList<PassScore>();
			if (m2.matches()) {
				for (String info : m2.group(3).split("<TR>")) {
					i = 0;
					PassScore passScore = new PassScore();
					for (String info2 : info.split("</td>")) {

						i++;
						m3 = p3.matcher(info2);
						m4 = p4.matcher(info2);
						if (m3.matches()) {
							strTemp = m3.group(3).replaceAll("&nbsp;", " ")
									.trim();
						} else if (m4.matches()) {
							strTemp = m4.group(3).replaceAll("&nbsp;", " ")
									.trim();
						}
						switch (i) {
						case 1:
							passScore.setClass_num(strTemp);
							break;
						case 2:
							passScore.setClass_name(strTemp);
							break;
						case 3:
							passScore.setNum(strTemp);
							break;
						case 4:
							passScore.setXf(strTemp);
							break;
						case 5:
							passScore.setKssj(strTemp);
							break;
						case 6:
							passScore.setScore(strTemp);
							break;
						case 7:
							passScore.setCh_class_num(strTemp);
							break;
						case 8:
							passScore.setRemark(strTemp);
							break;
						}
					}
					listPassScore2.add(passScore);
				}
			}
			listPassScore2.remove(0);
			listPassScore.addAll(listPassScore2);
			String returnStr = xfTotal;
			for (PassScore p : listPassScore) {
				returnStr += p.getClass_name() + ":" + p.getScore() + "\n";
			}
			strAll[1] = returnStr;
		}
		return strAll;
	}

	// 课表
	public static String[] getClassSchedule(String openID) {
		String strAll[] = goToBind(openID, class_schedule);
		if (!strAll[0].equals("0")) {
			String xfTotal = "";
			Pattern p0 = Pattern
					.compile("(.*)(<span class=\"td1\">你共选择了)(.*)(学分</span><BR>)(.*)");
			Matcher m0 = p0.matcher(strAll[1]);
			if (m0.matches()) {
				xfTotal = m0.group(3).trim();
			}

			Pattern p1 = Pattern
					.compile("(.*)(<td  height=\"25\"  class=td_hz_bj><p align=\"center\">选课限制说明</p></td>)(.*)(<tr><td height=\"25\" class=td_bt_bj align=center colspan=9>未安排时间地点的课程信息如下</td></tr>)(.*)");
			Matcher m1 = p1.matcher(strAll[1]);
			Pattern p3 = Pattern
					.compile("(.*)(<td width=\"112\" height=\"20\" class=td_biaogexian><p align=\"center\">)(.*)(</p>)(.*)");
			Pattern p4 = Pattern
					.compile("(.*)(<td width=\"112\" height=\"20\" class=td_biaogexian><p align=\"center\"><FONT COLOR=\"#FF0000\"></FONT>)(.*)(</p>)(.*)");
			Matcher m3 = null;
			Matcher m4 = null;
			int i = 0;
			String strTemp = "";
			List<ClassSchedule> listClassSchedule = new ArrayList<ClassSchedule>();
			if (m1.matches()) {
				for (String info : m1.group(3).split("</TR>")) {
					i = 0;
					ClassSchedule classSchedule = new ClassSchedule();
					for (String info2 : info.split("</td>")) {
						i++;
						m3 = p3.matcher(info2);
						m4 = p4.matcher(info2);
						if (m4.matches()) {
							strTemp = m4.group(3).replaceAll("&nbsp;", " ")
									.trim();
						} else if (m3.matches()) {
							strTemp = m3.group(3).replaceAll("&nbsp;", " ")
									.trim();
						}

						switch (i) {
						case 1:
							classSchedule.setClass_name(strTemp);
							break;
						case 2:
							classSchedule.setClass_num(strTemp);
							break;
						case 3:
							classSchedule.setKxh(strTemp);
							break;
						case 4:
							classSchedule.setKslx(strTemp);
							break;
						case 5:
							classSchedule.setAddr(strTemp);
							break;
						case 6:
							classSchedule.setTime(strTemp);
							break;
						case 7:
							classSchedule.setWeekly(strTemp);
							break;
						case 8:
							classSchedule.setRemark(strTemp);
							break;
						}
					}
					listClassSchedule.add(classSchedule);
				}
			}
			String returnStr = "你共选择了" + xfTotal + "学分\n\n";

			if (!xfTotal.equals("0")) {
				for (ClassSchedule c : listClassSchedule) {
					returnStr += c.getClass_name() + "\n地点:" + c.getAddr()
							+ "\n时间:" + c.getTime() + "\n周次:" + c.getWeekly()
							+ "\n考试类型:" + c.getKslx() + "\n\n";
				}
			}
			strAll[1] = returnStr;
		}
		return strAll;
	}

	public static String[] goToBind(String openID, String contentUrl) {
		String returnStr[] = new String[2];
		String bindUrl = ShmtuAuthService.auth_url_bind_pre + openID;
		// 检查是否绑定
		User user = UserDBUtil.getUserByOpenID(openID);
		if (user != null && user.getStudentPwd() != null
				&& !user.getStudentPwd().equals("")) {
			String str = ShmtuAuthService.getContentAfterAuthByHttpGet(
					user.getStudentNum(), user.getStudentPwd(), contentUrl);
			//System.out.println(str);
			// 帐号过期
			if (str != null && str.equals("0")) {
				returnStr[0] = "0";
				returnStr[1] = "你可能修改过密码了，请点击下面的链接重新绑定。\n\n" + "<a href=\""
						+ bindUrl
						+ "\">——>去绑定学号</a>\n\n如果你点击上面的链接无法完成绑定，请尝试下面的这种方式：\n"
						+ WeixinUtil.MSG_CONTENT_BIND_STUID_HELP;
			} else {
				returnStr[0] = "1";
				returnStr[1] = str;
			}
			// 提示绑定学号
		} else {
			returnStr[0] = "0";
			returnStr[1] = "你还没有绑定学号，请点击下面的链接去绑定。\n\n" + "<a href=\"" + bindUrl
					+ "\">——>去绑定学号</a>\n\n如果你点击上面的链接无法完成绑定，请尝试下面的这种方式：\n"
					+ WeixinUtil.MSG_CONTENT_BIND_STUID_HELP;
		}
		return returnStr;
	}

	public static String getContentAfterAuthByHttpGet(String stuid, String pwd,
			String contentUrl) {
		String returnStr = null;
		String auth_url_pass = auth_url.replace("{stuid}", stuid).replace(
				"{pwd}", pwd);
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(auth_url_pass);
		// 设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			// 执行getMethod完成认证
			httpClient.executeMethod(getMethod);
			byte[] responseBody = getMethod.getResponseBody();
			if (new String(responseBody).contains("你输入了错误的学号或密码")) {
				returnStr = "0";
			} else {
				GetMethod getMethodForContent = new GetMethod(contentUrl);
				httpClient.executeMethod(getMethodForContent);

				StringBuffer returnStrBuffer = new StringBuffer();
				InputStream inputStream = getMethodForContent
						.getResponseBodyAsStream();
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream, "gbk");
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					returnStrBuffer.append(str);
				}
				bufferedReader.close();
				inputStreamReader.close();
				inputStream.close();
				inputStream = null;

				returnStr = new String(returnStrBuffer);
			}
		} catch (HttpException e) {
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return returnStr;
	}

	public static String getStuInfoByHttpGet(String stuid, String pwd) {
		String returnStr = null;
		String auth_url_pass = auth_url.replace("{stuid}", stuid).replace(
				"{pwd}", pwd);
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(auth_url_pass);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			httpClient.executeMethod(getMethod);
			StringBuffer returnStrBuffer = new StringBuffer();
			InputStream inputStream = getMethod.getResponseBodyAsStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "gbk");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				returnStrBuffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			Pattern p1 = Pattern
					.compile("(.*)(<h4><font color=\"#9900FF\">)(.*)(</font></h4><br>)(.*)");
			Matcher m1 = p1.matcher(returnStrBuffer);
			if (m1.matches()) {
				returnStr = m1.group(3).replaceAll("&nbsp;", " ").trim();
			}
		} catch (HttpException e) {
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return returnStr;
	}

	/*
	 * import org.apache.commons.httpclient.methods.PostMethod; import
	 * org.apache.commons.httpclient.NameValuePair; import
	 * co.xiaodao.weixin.util.XiaoDaoUtil; public static boolean
	 * authByPost(String stuid, String pwd) { HttpClient httpClient = new
	 * HttpClient(); PostMethod postMethod = new PostMethod(auth_url_short); //
	 * 填入各个表单域的值 NameValuePair[] data = { new NameValuePair("stuid", stuid), new
	 * NameValuePair("pwd", pwd) }; // 将表单的值放入postMethod中
	 * postMethod.setRequestBody(data); try { // 执行postMethod int statusCode =
	 * httpClient.executeMethod(postMethod); //
	 * HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发 if (statusCode == 302) { //
	 * 认证成功 return true; } } catch (HttpException e) { //
	 * 发生致命的异常，可能是协议不对或者返回的内容有问题
	 * System.out.println("Please check your provided http address!");
	 * e.printStackTrace(); } catch (IOException e) { // 发生网络异常
	 * e.printStackTrace(); } finally { // 释放连接 postMethod.releaseConnection();
	 * } return false; }
	 */
}
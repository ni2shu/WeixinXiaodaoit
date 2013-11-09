package co.xiaodao.weixin.db.pojo;

/**
 * 用户信息，对应数据库表：tb_user
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class User {
	private String openID;
	private String username;
	private String name;
	private String password;
	private String createTime;
	private String updateTime;
	// 订阅状态：1表示订阅，0表示退订
	private String isSubscribe;
	// 学号
	private String studentNum;
	// 学号对应的密码
	private String studentPwd;
	// 学生信息
	private String studentInfo;
	// 学生院系
	private String studentDept;
	// 学生专业
	private String studentMajor;
	// 用户日志
	private String userLog;

	public User(String openID, String username, String password,
			String createTime, String updateTime, String isSubscribe,
			String name, String studentNum, String studentPwd,
			String studentInfo, String studentDept, String studentMajor,
			String userLog) {
		super();
		this.openID = openID;
		this.username = username;
		this.name = name;
		this.password = password;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.isSubscribe = isSubscribe;
		this.studentNum = studentNum;
		this.studentPwd = studentPwd;
		this.studentInfo = studentInfo;
		this.studentDept = studentDept;
		this.studentMajor = studentMajor;
		this.userLog = userLog;
	}

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setStudentNum(String studentNum) {
		this.studentNum = studentNum;
	}

	public String getStudentNum() {
		return studentNum;
	}

	public void setStudentPwd(String studentPwd) {
		this.studentPwd = studentPwd;
	}

	public String getStudentPwd() {
		return studentPwd;
	}

	public String getStudentInfo() {
		return studentInfo;
	}

	public void setStudentInfo(String studentInfo) {
		this.studentInfo = studentInfo;
	}

	public String getUserLog() {
		return userLog;
	}

	public void setUserLog(String userLog) {
		this.userLog = userLog;
	}

	public String getStudentDept() {
		return studentDept;
	}

	public void setStudentDept(String studentDept) {
		this.studentDept = studentDept;
	}

	public String getStudentMajor() {
		return studentMajor;
	}

	public void setStudentMajor(String studentMajor) {
		this.studentMajor = studentMajor;
	}

}

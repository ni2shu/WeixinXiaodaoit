package co.xiaodao.weixin.json.dianping;

import java.util.List;

/**
 * 大众点评JSON实体类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class DianPingJson {
	// 本次API访问状态，如果成功返回"OK"，并返回结果字段，如果失败返回"ERROR"，并返回错误说明
	private String status;
	// 本次API访问所获取的商户数量
	private int count;
	// 单个商户信息集合
	private List<DianPingBusinesses> businesses;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<DianPingBusinesses> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(List<DianPingBusinesses> businesses) {
		this.businesses = businesses;
	}
}

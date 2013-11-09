package co.xiaodao.weixin.json.dianping;

/**
 * 大众点评商家的团购信息实体类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class DianPingDeal {

	// 团购ID
	private String deals_id;
	// 团购描述
	private String deals_description;
	// 团购页面链接
	private String deals_url;

	public String getDeals_id() {
		return deals_id;
	}

	public void setDeals_id(String deals_id) {
		this.deals_id = deals_id;
	}

	public String getDeals_description() {
		return deals_description;
	}

	public void setDeals_description(String deals_description) {
		this.deals_description = deals_description;
	}

	public String getDeals_url() {
		return deals_url;
	}

	public void setDeals_url(String deals_url) {
		this.deals_url = deals_url;
	}

}

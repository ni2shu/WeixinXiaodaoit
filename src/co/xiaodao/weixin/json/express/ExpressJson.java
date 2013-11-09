package co.xiaodao.weixin.json.express;

import java.util.List;

/**
 * 快递JSON实体类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class ExpressJson {
	// 快递代号，如：圆通（yuantong）、申通（shentong）
	private String id;
	// 快递名称
	private String name;
	// 快递单号，请注意区分大小写
	private String order;
	// 当天已使用次数
	private String num;
	// 该记录最后查询时间（在本系统30分钟内同一个快递同一个单号多次将查询返回缓存数据）
	private String updateTime;
	// 输出消息内容（可忽略）
	private String message;
	/*
	 * 返回错误码： 0：无错误， 1：快递KEY无效， 2：快递代号无效， 3：访问次数达到最大额度， 4：查询服务器返回错误即返回状态码非200,
	 * 5：程序执行出错
	 */
	private String errCode;
	/*
	 * 订单跟踪状态： 0：查询出错（即errCode!=0）， 1：暂无记录， 2：在途中， 3：派送中， 4：已签收， 5：拒收， 6：疑难件
	 * 7：退回
	 */
	private String status;
	// 订单跟踪数据集合
	private List<ExpressSingleData> data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ExpressSingleData> getData() {
		return data;
	}

	public void setData(List<ExpressSingleData> data) {
		this.data = data;
	}
}

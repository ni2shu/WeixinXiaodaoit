package co.xiaodao.weixin.json.dianping;

import java.util.List;

/**
 * 大众点评商家的实体类
 * 
 * 小道IT微信公众号码:『xiaodaoit』
 * 
 * @UpdateDate: 2013-4-21
 * @author: 陈小道
 * @Email: xiaodaoit@gmail.com
 * @Copyright 2012-2015(C)xiaodao.co. All Rights Reserved.
 * 
 */
public class DianPingBusinesses {

	// 商户ID
	private int business_id;
	// 商户名
	private String name;
	// 分店名
	private String branch_name;
	// 地址
	private String address;
	// 带区号的电话
	private String telephone;
	// 所在城市
	private String city;
	// 所在区域信息列表，如[徐汇区，徐家汇]
	private String[] regions;
	// 所属分类信息列表，如[宁波菜，婚宴酒店]
	private String[] categories;
	// 纬度坐标
	private float latitude;
	// 经度坐标
	private float longitude;
	// 星级评分，5.0代表五星，4.5代表四星半，依此类推
	private float avg_rating;
	// 星级图片链接
	private String rating_img_url;
	// 小尺寸星级图片链接
	private String rating_s_img_url;
	// 产品/食品口味评价，1:一般，2:尚可，3:好，4:很好，5:非常好
	private int product_grade;
	// 环境评价，1:一般，2:尚可，3:好，4:很好，5:非常好
	private int decoration_grade;;
	// 服务评价，1:一般，2:尚可，3:好，4:很好，5:非常好
	private int service_grade;
	// 点评数量
	private int review_count;
	// 商户与参数坐标的距离，单位为米，如不传入经纬度坐标，结果为-1
	private int distance;
	// 商户页面链接
	private String business_url;
	// 照片链接，照片最大尺寸700×700
	private String photo_url;
	// 小尺寸照片链接，照片最大尺寸278×200
	private String s_photo_url;
	// 是否有优惠券，0:没有，1:有
	private int has_coupon;
	// 优惠券ID
	private int coupon_id;
	// 优惠券描述
	private String coupon_description;
	// 优惠券页面链接
	private String coupon_url;
	// 是否有团购，0:没有，1:有
	private int has_deal;
	// 商户当前在线团购数量
	private int deal_count;
	// 团购列表
	private List<DianPingDeal> deals;

	public int getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(int business_id) {
		this.business_id = business_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBranch_name() {
		return branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String[] getRegions() {
		return regions;
	}

	public void setRegions(String[] regions) {
		this.regions = regions;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getAvg_rating() {
		return avg_rating;
	}

	public void setAvg_rating(float avg_rating) {
		this.avg_rating = avg_rating;
	}

	public String getRating_img_url() {
		return rating_img_url;
	}

	public void setRating_img_url(String rating_img_url) {
		this.rating_img_url = rating_img_url;
	}

	public String getRating_s_img_url() {
		return rating_s_img_url;
	}

	public void setRating_s_img_url(String rating_s_img_url) {
		this.rating_s_img_url = rating_s_img_url;
	}

	public int getProduct_grade() {
		return product_grade;
	}

	public void setProduct_grade(int product_grade) {
		this.product_grade = product_grade;
	}

	public int getDecoration_grade() {
		return decoration_grade;
	}

	public void setDecoration_grade(int decoration_grade) {
		this.decoration_grade = decoration_grade;
	}

	public int getService_grade() {
		return service_grade;
	}

	public void setService_grade(int service_grade) {
		this.service_grade = service_grade;
	}

	public int getReview_count() {
		return review_count;
	}

	public void setReview_count(int review_count) {
		this.review_count = review_count;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getBusiness_url() {
		return business_url;
	}

	public void setBusiness_url(String business_url) {
		this.business_url = business_url;
	}

	public String getPhoto_url() {
		return photo_url;
	}

	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}

	public String getS_photo_url() {
		return s_photo_url;
	}

	public void setS_photo_url(String s_photo_url) {
		this.s_photo_url = s_photo_url;
	}

	public int getHas_coupon() {
		return has_coupon;
	}

	public void setHas_coupon(int has_coupon) {
		this.has_coupon = has_coupon;
	}

	public int getCoupon_id() {
		return coupon_id;
	}

	public void setCoupon_id(int coupon_id) {
		this.coupon_id = coupon_id;
	}

	public String getCoupon_description() {
		return coupon_description;
	}

	public void setCoupon_description(String coupon_description) {
		this.coupon_description = coupon_description;
	}

	public String getCoupon_url() {
		return coupon_url;
	}

	public void setCoupon_url(String coupon_url) {
		this.coupon_url = coupon_url;
	}

	public int getHas_deal() {
		return has_deal;
	}

	public void setHas_deal(int has_deal) {
		this.has_deal = has_deal;
	}

	public int getDeal_count() {
		return deal_count;
	}

	public void setDeal_count(int deal_count) {
		this.deal_count = deal_count;
	}

	public List<DianPingDeal> getDeals() {
		return deals;
	}

	public void setDeals(List<DianPingDeal> deals) {
		this.deals = deals;
	}

}

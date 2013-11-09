package co.xiaodao.weixin.db.pojo;

/**
 * –≈œ¢ª∫¥Ê±Ì
 * 
 * @author dao-zheng.chen
 * 
 */
public class MsgCache {

	private int id;
	private String openID;
	private String weather;
	private String express;

	public MsgCache(String openID, String weather, String express) {
		super();
		this.openID = openID;
		this.weather = weather;
		this.express = express;
	}

	public MsgCache(int id, String openID, String weather, String express) {
		super();
		this.id = id;
		this.openID = openID;
		this.weather = weather;
		this.express = express;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

}

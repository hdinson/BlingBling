package dinson.customview.entity;

import java.io.Serializable;



/**
 * 客户端信息参数
 * 
 * @author cguangcong@linewell.com
 * @since 2017-06-20
 */
public class ClientParams implements Serializable {
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -3091659206357789185L;

	/**
	 * 网络类型 wifi、4g、3g、2g
	 */
	private String network = "wifi";
	
	/**
	 * 设备标识
	 */
	private String deviceId;
	
	/**
	 * 浏览器类型
	 */
	private String browser;
	
	/**
	 * 系统类型 iOS,Android
	 */
	private String os;
	
	/**
	 * 客户端ip地址
	 */
	private String ip;
	
	/**
	 * app版本
	 */
	private String appVersion;
	
	/**
	 * 时间戳
	 */
	private long timeStamp;

	/**
	 * 版本号
	 * @return
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * 版本号
	 * @param appVersion
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * 客户端ip
	 * @return
	 */
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 获取wifi类型
	 * @return network wifi类型
	 */
	public String getNetwork() {
		return network;
	}

	/**
	 * 设置wifi类型
	 * @param network wifi类型
	 */
	public void setNetwork(String network) {
		this.network = network;
	}

	/**
	 * 获取设备标识
	 * @return deviceId 设备标识
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * 设置设备标识
	 * @param deviceId 设备标识
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * 获取浏览器类型
	 * @return  browser 浏览器类型
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * 设置浏览器类型
	 * @param browser 浏览器类型
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

	/**
	 * 获取系统类型
	 * @return os 系统类型
	 */
	public String getOs() {
		return os;
	}

	/**
	 * 设置系统类型
	 * @param os 系统类型
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * 获取时间戳
	 * @return
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * 设置时间戳
	 * @param timeStamp	时间戳
	 */
	public void setTimeStamp(long timeStamp) {
		if(0==timeStamp){
			this.timeStamp = System.currentTimeMillis();
		}else{
			this.timeStamp = timeStamp;
		}
	}
	
}

package dinson.customview.entity;

import java.io.Serializable;

/**
 * 登录验证参数
 * 
 * @author cguangcong@linewell.com
 * @since 2017-06-20
 */
public class AuthParams implements Serializable{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -4960164577938303941L;
	
	/**
	 * 登陆票据
	 */
	private String token;// 票据
	
	/**
	 * 第三方标识
	 */
	private String openId;//第三方登陆标识
	
	/**
	 * 第三方登陆标识
	 * @return
	 */
	public String getOpenId() {
		return openId;
	}

	/**
	 * 第三方登陆标识
	 * @param openId
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	/**
	 * 获取票据
	 * @return
	 */
	public String getToken() {
		return token;
	}

	/**
	 * 设置票据
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	
}

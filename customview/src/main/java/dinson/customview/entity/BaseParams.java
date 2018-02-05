package dinson.customview.entity;

import java.io.Serializable;

/**
 * 请求的基本参数
 * 
 * @author cguangcong@linewell.com
 * @since 2017-06-20
 *
 */
public class BaseParams implements Serializable {
	/**
     * 序列号
	 */
	private static final long serialVersionUID = -8116230898355165433L;

	/**
	 * 登录验证参数
	 */
	private AuthParams authParams;
	
	/**
	 * 客户端参数
	 */
	private ClientParams clientParams;
	
	/**
	 * 区域编号
	 */
	private String areaCode;
	
	/**
	 * 请求访问的url
	 */
	private String url;

	/**
	 * 站点英文名
	 */
	private String siteEn;

	/**
	 * 获取认证信息
	 * @return
	 */
	public AuthParams getAuthParams() {
		return authParams;
	}

	/**
	 * 设置认证信息
	 * @param authParams	认证信息
	 */
	public void setAuthParams(AuthParams authParams) {
		this.authParams = authParams;
	}

	/**
	 * 获取客户端信息
	 * @return
	 */
	public ClientParams getClientParams() {
		return clientParams;
	}

	/**
	 * 设置客户端信息
	 * @param clientParams	客户端信息
	 */
	public void setClientParams(ClientParams clientParams) {
		this.clientParams = clientParams;
	}

	/**
	 * 获取地区编号
	 * 
	 * @return
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * 设置区域编码
	 * 
	 * @param areaCode	区域编码
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * 获取请求访问的url
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置url
	 * @param url	url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public String getSiteEn() {
		return siteEn;
	}

	public void setSiteEn(String siteEn) {
		this.siteEn = siteEn;
	}
	
}

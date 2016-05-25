package org.platform.modules.auth.dto;

import java.io.Serializable;
import java.util.Date;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 用户ID*/
	private Long id;
	/** 用户名*/
	private String name;
	/**用户密码 */
	private String password;
	/**用户编码 */
	private String code;
	/**创建时间*/
	private Date createTime = null;
	/**过期时间*/
	private Date expireTime = null;
	/**用户有效标志 */
	private String availan;
	/**用户有效标志 */
	private String availanCN;

	public UserDTO() {
	}

	public UserDTO(Long id, String name, String password, String code,
			Date createTime, Date expireTime, String availan) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.code = code;
		this.createTime = createTime;
		this.expireTime = expireTime;
		this.availan = availan;
	}

	public UserDTO(Long id, String name, String password, String code,
			Date createTime, Date expireTime, String availan, String availanCN) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.code = code;
		this.createTime = createTime;
		this.expireTime = expireTime;
		this.availan = availan;
		this.availanCN = availanCN;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAvailan() {
		return availan;
	}

	public void setAvailan(String availan) {
		this.availan = availan;
	}

	public String getAvailanCN() {
		return availanCN;
	}

	public void setAvailanCN(String availanCN) {
		this.availanCN = availanCN;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

}

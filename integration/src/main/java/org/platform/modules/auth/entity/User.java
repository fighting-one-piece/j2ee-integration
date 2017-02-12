package org.platform.modules.auth.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.platform.modules.abstr.entity.IdAutoEntity;

@Entity
@Table(name="T_PLAT_USER")
public class User extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;

	/**用户名称 */
	@Column(name="NAME")
	private String name = null;
	/**用户密码 */
	@Column(name="PASSWORD")
	private String password = null;
	/**用户标识 */
	@Column(name="IDENTITY")
	private String identity = null;
	/** 用户昵称*/
	@Column(name="NICK_NAME")
	private String nickName = null;
	/** 用户邮箱*/
	@Column(name="EMAIL")
	private String email = null;
	/**创建时间*/
	@Column(name="CREATE_TIME")
	private Date createTime = null;
	/**过期时间*/
	@Column(name="EXPIRE_TIME")
	private Date expireTime = null;
	/**用户有效标志 */
	@Column(name="AVAILAN")
	private Boolean availan = Boolean.FALSE;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Boolean getAvailan() {
		return availan;
	}
	
	public void setAvailan(Boolean availan) {
		this.availan = availan;
	}

}

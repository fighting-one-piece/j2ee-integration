package org.platform.modules.system.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.platform.modules.abstr.entity.IdAutoEntity;
import org.platform.modules.auth.entity.User;

@Entity
@Table(name="T_PLAT_EMPLOYEE")
@Access(AccessType.FIELD)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Employee extends IdAutoEntity<Long> {

	private static final long serialVersionUID = 1L;

	/** 职员名称*/
	@Column(name="NAME")
	private String name = null;
	/** 职员性别*/
	@Column(name="GENDER")
	private String gender = null;
	/** 职员年龄*/
	@Column(name="AGE")
	private Integer age = null;
	/** 职员电话*/
	@Column(name="PHONE")
	private String phone = null;
	/** 职员邮箱*/
	@Column(name="EMAIL")
	private String email = null;
	/** 职员地址*/
	@Column(name="ADDRESS")
	private String address = null;
	/** 职员机构*/
	@ManyToOne(cascade=CascadeType.REFRESH, optional=true)
	@JoinColumn(name="ORG_ID", unique=true)
	private Organization organization = null;
	/** 职员用户*/
	@OneToOne(cascade=CascadeType.REFRESH, optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="USER_ID", unique=true)
	private User user = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}

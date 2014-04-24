package org.platform.modules.auth.dto;

import java.io.Serializable;

public class RoleDTO implements Serializable {


	private static final long serialVersionUID = 1L;

	/**角色ID */
	private Long id;
	/**角色名称 */
	private String name;
	/**角色编码 */
	private String code;
	/**角色描述 */
	private String desc;
	/**角色有效标志 */
	private String availan;
	/**角色有效标志 */
	private String availanCN;

	public RoleDTO(){
	}

	public RoleDTO(Long id, String name, String code, String desc,
			String availan) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.desc = desc;
		this.availan = availan;
	}

	public RoleDTO(Long id, String name, String code, String desc,
			String availan, String availanCN) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.desc = desc;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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


}

package org.platform.modules.abstr.web.utils;

import java.io.Serializable;

public class WebResult implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 结果代码*/
	private Integer code = null;
	/** 错误信息*/
	private String failure = null;
	/** 结果数据*/
	private Object data = null;

	public WebResult() {
		this.code = ResultCode.SUCCESS.getCode();
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		if (code != null && (code == 200 || code == 201)) {
			this.code = ResultCode.SUCCESS.getCode();
			return;
		}
		this.code = code;
	}

	public String getFailure() {
		return failure;
	}

	public void setFailure(String failure) {
		this.failure = failure;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		NullUtil.removeNull(data);
		this.data = data;
	}

	public void setResultCode(ResultCode code) {
		if (code.getCode() == 200 || code.getCode() == 201) {
			this.code = ResultCode.SUCCESS.getCode();
		} else {
			this.code = code.getCode();
		}
		this.failure = code.getDesc();
	}


}

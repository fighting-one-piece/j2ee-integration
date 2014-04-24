package org.platform.utils.template;

import java.util.Map;

public interface ITemplateEngine {

	/**
	 *
	  *<p>包名类名：platform.utils.template.ITemplateEngine</p>
	  *<p>方法名：getTemplate</p>
	  *<p>描述：获取模版文件内容</p>
	  *<p>参数：@param data 模版文件数据
	  *<p>参数：@param name 模版文件名称
	  *<p>参数：@return</p> 模版文件
	  *<p>返回类型：String</p>
	  *<p>创建时间：2013-3-16 下午11:52:16</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public String getTemplate(Map<String, Object> data, String name);
}

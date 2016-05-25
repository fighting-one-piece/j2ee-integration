package org.platform.modules.abstr.web.controller;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.platform.entity.Query;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.biz.IGenericBusiness;
import org.platform.utils.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class GenericController<Entity extends Serializable, PK extends Serializable> {

	/** 日志*/
	protected Logger LOG = LoggerFactory.getLogger(getClass());

	protected Class<Entity> entityClass = null;
	
	private String viewPrefix = null;
	
	@SuppressWarnings("unchecked")
	public GenericController() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            entityClass = (Class<Entity>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        setViewPrefix(defaultViewPrefix());
	}
	
	public abstract IGenericBusiness<Entity, PK> obtainBusinessInstance();
	
	protected Entity newEntity() {
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("can not instantiated entity : " + this.entityClass, e);
        }
    }
	
	protected String defaultViewPrefix() {
        String currentViewPrefix = "";
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(getClass(), RequestMapping.class);
        if (null != requestMapping && requestMapping.value().length > 0) {
            currentViewPrefix = requestMapping.value()[0];
        }
        if (StringUtils.isEmpty(currentViewPrefix)) {
            currentViewPrefix = this.entityClass.getSimpleName();
        }
        return currentViewPrefix;
    }
	
	/**
     * @param backURL null 将重定向到默认getViewPrefix()
     * @return
     */
    protected String redirectToUrl(String backURL) {
        if (StringUtils.isEmpty(backURL)) backURL = getViewPrefix();
        if (!backURL.startsWith("/") && !backURL.startsWith("http")) {
            backURL = "/" + backURL;
        }
        return "redirect:" + backURL;
    }
    
    /**
     * 当前模块 视图的前缀 默认
     * 1、获取当前类头上的@RequestMapping中的value作为前缀
     * 2、如果没有就使用当前模型小写的简单类名
     */
    public void setViewPrefix(String viewPrefix) {
        if (viewPrefix.startsWith("/")) viewPrefix = viewPrefix.substring(1);
        this.viewPrefix = viewPrefix;
    }

    public String getViewPrefix() {
        return viewPrefix;
    }
    
	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public String insert(Model model) {
		model.addAttribute("entity", newEntity());
		return defaultViewPrefix() + "/insert";
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insert(@Valid Entity entity, BindingResult result) {
		if (result.hasErrors()) return defaultViewPrefix() + "/insert";
		try {
			obtainBusinessInstance().insert(entity);
		} catch (BusinessException be) {
			
		} catch (Exception e) {
			
		}
		return redirectToUrl(defaultViewPrefix());
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
	public String update(@PathVariable PK id, Model model) {
		try {
			Entity entity = (Entity) obtainBusinessInstance().readDataByPK(id, false);
			model.addAttribute("entity", entity);
		} catch (BusinessException be) {
			
		} catch (Exception e) {
			
		}
		return defaultViewPrefix() + "/insert";
	}

	@RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
	public String update(@Valid Entity entity, BindingResult result) {
		if (result.hasErrors()) return defaultViewPrefix() + "/insert";
		try {
			obtainBusinessInstance().update(entity);
		} catch (BusinessException be) {
			
		} catch (Exception e) {
			
		}
		return redirectToUrl(defaultViewPrefix());
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable PK id) {
		try {
			obtainBusinessInstance().deleteByPK(id);
		} catch (BusinessException be) {
			
		} catch (Exception e) {
			
		}
		return redirectToUrl(defaultViewPrefix());
	}
	
	@ResponseBody
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{pk}/json", method = RequestMethod.GET)
	public Entity readDataByPKWithJson(@PathVariable PK pk) {
		return (Entity) obtainBusinessInstance().readDataByPK(pk, false);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/p/{currentPageNum}/r/{rowNumPerPage}", method = RequestMethod.GET)
	public String readDataListWithPagination(@PathVariable int currentPageNum, @PathVariable int 
			rowNumPerPage, Model model) {
		Query query = new Query();
		query.setPagination(true);
		query.setCurrentPageNum(currentPageNum);
		query.setRowNumPerPage(rowNumPerPage);
		QueryResult<Entity> qr = (QueryResult<Entity>) 
				obtainBusinessInstance().readDataPaginationByCondition(query, false);
		model.addAttribute("currentPageNum", currentPageNum);
		model.addAttribute("rowNumPerPage", rowNumPerPage);
		model.addAttribute("totalRowNum", qr.getTotalRowNum());
		model.addAttribute("entities", qr.getResultList());
		return defaultViewPrefix() + "/list";
	}
	
	@ResponseBody
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/p/{currentPageNum}/r/{rowNumPerPage}/json", method = RequestMethod.GET)
	public List<Entity> readDataListWithPaginationAndJson(@PathVariable int currentPageNum, @PathVariable 
			int rowNumPerPage) {
		Query query = new Query();
		query.setPagination(true);
		query.setCurrentPageNum(currentPageNum);
		query.setRowNumPerPage(rowNumPerPage);
		QueryResult<Entity> qr = (QueryResult<Entity>) 
				obtainBusinessInstance().readDataListByCondition(query, false);
		return qr.getResultList();
	}
	
}

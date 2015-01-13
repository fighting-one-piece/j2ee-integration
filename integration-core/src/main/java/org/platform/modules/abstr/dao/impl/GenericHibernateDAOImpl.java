package org.platform.modules.abstr.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.platform.entity.Query;
import org.platform.entity.QueryItem;
import org.platform.entity.QueryResult;
import org.platform.modules.abstr.dao.IGenericDAO;
import org.platform.utils.exception.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository("genericHibernateDAO")
public class GenericHibernateDAOImpl<Entity extends Serializable, PK extends Serializable> implements IGenericDAO<Entity, PK> {

	/** 日志记录*/
	protected Logger LOG = Logger.getLogger(getClass());

	@Resource(name="sessionFactory")
	protected SessionFactory sessionFactory = null;

	/** 实体类*/
	protected Class<Entity> entityClass = null;

	@SuppressWarnings("unchecked")
	public GenericHibernateDAOImpl() {
		Type t = getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            this.entityClass = (Class<Entity>) ((ParameterizedType) t).getActualTypeArguments()[0];
        }
	}

	@Override
	public void insert(Entity entity) throws DataAccessException {
		Session session = sessionFactory.getCurrentSession();
		session.save(entity);
	}
	
	public void insert(java.util.List<Entity> entities) throws DataAccessException {
		
	};

	@Override
	public void update(Entity entity) throws DataAccessException {
		Session session = sessionFactory.getCurrentSession();
		session.update(entity);
	}
	
	@Override
	public void update(List<Entity> entities) throws DataAccessException {
		
	}

	@Override
	public void delete(Entity entity) throws DataAccessException {
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
	}

	@Override
	public void deleteByPK(PK pk) throws DataAccessException {
		Session session = sessionFactory.getCurrentSession();
		session.delete(readDataByPK(pk));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Entity readDataByPK(PK pk) throws DataAccessException {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		return (Entity) criteria.add(Restrictions.eq("id", pk)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Entity readDataByCondition(Query query) throws DataAccessException {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		addRestrictions(criteria, query);
		return (Entity) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> readDataListByCondition(Query query) throws DataAccessException {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		addRestrictions(criteria, query);
		List<Entity> resultList = criteria.list();
		return null == resultList ? new ArrayList<Entity>() : resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public QueryResult<Entity> readDataPaginationByCondition(Query query) throws DataAccessException {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		addRestrictions(criteria, query);
		CriteriaImpl impl = (CriteriaImpl) criteria;
        //先把Projection和OrderBy条件取出来,清空两者来执行Count操作
        Projection projection = impl.getProjection();
		int totalRowNum = ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

		criteria.setProjection(projection);
        if (projection == null) {
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
		int firstRowNum = (query.getCurrentPageNum() - 1) * query.getRowNumPerPage();
		List<Entity> resultList = criteria.setFirstResult(firstRowNum)
				.setMaxResults(query.getRowNumPerPage()).list();
		return new QueryResult<Entity>(totalRowNum, resultList);
	}
	
	@Override
	public Long readCountByCondition(Query query) throws DataAccessException {
		return null;
	}

	@Override
	public void flush() throws DataAccessException {
		Session session = sessionFactory.getCurrentSession();
		session.flush();
	}

	/**
	 *<p>包名类名：org.platform.modules.abstr.dao.impl.GenericDAOImpl</p>
	 *<p>  方法名：addRestrictions</p>
	 *<p>    描述：添加条件限制</p>
	 *<p>    参数：@param criteria
	 *<p>    参数：@param queryCondition</p>
	 *<p>返回类型：void</p>
	 *<p>创建时间：2012-11-24 上午12:16:14</p>
	 *<p>    作者: wl </p>
	 */
	private void addRestrictions(Criteria criteria, Query query) {
		if (query.getHibernateCondition().size() == 0) {
			return;
		}
		Map<String, QueryItem> conditionMap = query.getHibernateCondition();
		for (Map.Entry<String, QueryItem> entry : conditionMap.entrySet()) {
			criteria.add(addRestriction(entry.getValue()));
		}
		if (query.getOrderCondition() != null) {
			if (query.getOrderType() == Query.ORDER_ASC) {
				criteria.addOrder(Order.asc(query.getOrderCondition()));
			} else {
				criteria.addOrder(Order.desc(query.getOrderCondition()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Criterion addRestriction(QueryItem queryItem) {
		Criterion criterion = null;
		String conditionKey = queryItem.getConditionKey();
		Object conditionValue = queryItem.getConditionValue();
		int matchType = queryItem.getMatchType();
		switch (matchType) {
			case QueryItem.MATCH_NE :
				criterion = Restrictions.ne(conditionKey, conditionValue);
				break;
			case QueryItem.MATCH_GT :
				criterion = Restrictions.gt(conditionKey, conditionValue);
				break;
			case QueryItem.MATCH_GE :
				criterion = Restrictions.ge(conditionKey, conditionValue);
				break;
			case QueryItem.MATCH_LT :
				criterion = Restrictions.lt(conditionKey, conditionValue);
			case QueryItem.MATCH_LE :
				criterion = Restrictions.le(conditionKey, conditionValue);
				break;
			case QueryItem.MATCH_BETWEEN :
				Object[] conditionValueBetween = (Object[]) conditionValue;
				criterion = Restrictions.between(conditionKey, conditionValueBetween[0], conditionValueBetween[1]);
				break;
			case QueryItem.MATCH_IN :
				Object[] conditionValueIn = (Object[]) conditionValue;
				criterion = Restrictions.in(conditionKey, conditionValueIn);
				break;
			case QueryItem.MATCH_OR :
				List<QueryItem> conditionValueOr = (List<QueryItem>) conditionValue;
				Disjunction disjunctionOr = Restrictions.disjunction();
				for (QueryItem queryItemOr : conditionValueOr) {
					disjunctionOr.add(addRestriction(queryItemOr));
				}
				criterion = Restrictions.or(disjunctionOr);
				break;
			case QueryItem.MATCH_AND :
				List<QueryItem> conditionValueAnd = (List<QueryItem>) conditionValue;
				Disjunction disjunctionAnd = Restrictions.disjunction();
				for (QueryItem queryItemAnd : conditionValueAnd) {
					disjunctionAnd.add(addRestriction(queryItemAnd));
				}
				criterion = Restrictions.and(disjunctionAnd);
				break;
			case QueryItem.MATCH_ISNULL :
				criterion = Restrictions.isNull(conditionKey);
				break;
			case QueryItem.MATCH_ISNOTNULL :
				criterion = Restrictions.isNotNull(conditionKey);
				break;
			case QueryItem.MATCH_LIKE :
				criterion = Restrictions.like(conditionKey, (String) conditionValue, MatchMode.ANYWHERE);
				break;
			case QueryItem.MATCH_LIKE_START :
				criterion = Restrictions.like(conditionKey, (String) conditionValue, MatchMode.START);
				break;
			case QueryItem.MATCH_LIKE_END :
				criterion = Restrictions.like(conditionKey, (String) conditionValue, MatchMode.END);
				break;
			default :
				criterion = Restrictions.eq(conditionKey, conditionValue);
				break;
		}
		return criterion;
	}

}

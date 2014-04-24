package org.platform.modules.auth.dao.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.platform.modules.abstr.dao.impl.GenericHibernateDAOImpl;
import org.platform.modules.auth.dao.IGroupDAO;
import org.platform.modules.auth.entity.Group;
import org.platform.utils.exception.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository("groupHibernateDAO")
public class GroupDAOImpl extends GenericHibernateDAOImpl<Group, Long> implements IGroupDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Group> readDataListByUserId(Long userId) throws DataAccessException {
		String hql = "select g.id,g.name,g.identity from Group as g right outer join g.userGroups as ug where ug.user.id=:userId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("userId", userId);
		return convert(query.list());
	}
	
	private List<Group> convert(List<Object[]> resultList) {
		List<Group> groups = new ArrayList<Group>();
		for (Object[] result : resultList) {
			Group group = new Group();
			group.setId((Long) result[0]);
			group.setName((String) result[1]);
			group.setIdentity((String) result[2]);
			groups.add(group);
		}
		return groups;
	}

}

package org.platform.modules.auth.dao.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.platform.modules.abstr.dao.impl.GenericHibernateDAOImpl;
import org.platform.modules.auth.dao.IRoleDAO;
import org.platform.modules.auth.entity.Role;
import org.platform.utils.exception.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository("roleHibernateDAO")
public class RoleDAOImpl extends GenericHibernateDAOImpl<Role, Long> implements IRoleDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> readDataListByUserId(Long userId) throws DataAccessException {
		String hql = "select r.id,r.name,r.identity from Role as r right outer join r.userRoles as ur where ur.user.id=:userId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("userId", userId);
		return convert(query.list());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> readDataListByGroupId(Long groupId) throws DataAccessException {
		String hql = "select r.id,r.name,r.identity from Role as r right outer join r.groupRoles as gr where gr.group.id=:groupId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("groupId", groupId);
		return convert(query.list());
	}
	
	private List<Role> convert(List<Object[]> resultList) {
		List<Role> roles = new ArrayList<Role>();
		for (Object[] result : resultList) {
			Role role = new Role();
			role.setId((Long) result[0]);
			role.setName((String) result[1]);
			role.setIdentity((String) result[2]);
			roles.add(role);
		}
		return roles;
	}

}

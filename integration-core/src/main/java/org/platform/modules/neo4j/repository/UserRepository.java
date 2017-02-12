package org.platform.modules.neo4j.repository;

import org.platform.modules.neo4j.entity.IQiYiUser;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface UserRepository extends GraphRepository<IQiYiUser> {

}

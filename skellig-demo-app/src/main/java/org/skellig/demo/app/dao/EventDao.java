package org.skellig.demo.app.dao;

import org.skellig.demo.app.dao.model.EventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDao extends CrudRepository<EventEntity, String> {
}

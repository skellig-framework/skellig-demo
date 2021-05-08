package org.skellig.demo.app.dao;

import org.skellig.demo.app.dao.model.EventChangedLog;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventChangedLogDao extends CassandraRepository<EventChangedLog, Integer> {
}

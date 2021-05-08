package org.skellig.demo.app.dao;

import org.skellig.demo.app.dao.model.BookingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDao extends CrudRepository<BookingEntity, Long> {
}

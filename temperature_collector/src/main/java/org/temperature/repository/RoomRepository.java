package org.temperature.repository;

import org.springframework.data.repository.CrudRepository;
import org.temperature.model.db.Room;

public interface RoomRepository extends CrudRepository<Room, Long> {
}

package org.temperature.repository;

import org.springframework.data.repository.CrudRepository;
import org.temperature.model.db.Room;
import org.temperature.model.db.Thermometer;

public interface RoomRepository extends CrudRepository<Room, Long> {

  Room findByIdentifier(String identifier);

}

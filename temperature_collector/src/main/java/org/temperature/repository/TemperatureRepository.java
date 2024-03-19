package org.temperature.repository;

import org.springframework.data.repository.CrudRepository;
import org.temperature.model.db.Temperature;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {
}

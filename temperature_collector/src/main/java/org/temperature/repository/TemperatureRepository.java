package org.temperature.repository;

import org.springframework.data.repository.CrudRepository;
import org.temperature.model.db.Temperature;
import org.temperature.model.db.Thermometer;

import java.util.List;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

  List<Temperature> findByThermometerId(Long thermometerId);

}

package org.temperature.repository;

import org.springframework.data.repository.CrudRepository;
import org.temperature.model.db.Thermometer;

public interface ThermometerRepository extends CrudRepository<Thermometer, Long> {

  Thermometer findByThermometerName(String thermometerName);

}

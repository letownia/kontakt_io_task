package org.temperature.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.temperature.model.db.Thermometer;

public interface ThermometerRepository extends CrudRepository<Thermometer, Long> {

  Thermometer findByIdentifier(String identifier);
}

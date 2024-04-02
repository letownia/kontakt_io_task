package org.temperature.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.temperature.model.db.Temperature;

import java.util.List;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

  @Query(
      value = "SELECT * FROM TEMPERATURE LIMIT ?1",
      nativeQuery = true)
  List<Temperature> getXTemperatures(int X);

  @Query(
      value = "SELECT * FROM TEMPERATURE where thermometer_id=?3 AND id <= ?2 ORDER by id desc LIMIT ?1",
      nativeQuery = true)
  List<Temperature> getXPreviousTemperatures(int X, long temperatureId, long thermometerId);

//
//  @Query(
//      value = "SELECT * FROM TEMPERATURE where thermometer_id=?2 and  timestamp_ms >= ?1 ORDER by timestamp_ms desc",
//      nativeQuery = true)
//  List<Temperature> getTemperaturesNewerThan(long timestampMs, long thermometerId);

  @Query(
      value = "SELECT * FROM TEMPERATURE where thermometer_id=?3 and timestamp_ms >= ?1 and timestamp_ms <= ?2 ORDER by timestamp_ms desc",
      nativeQuery = true)
  List<Temperature> getTemperaturesInRange(long startTimeMs, long endTimeMs, long thermometerId);

  List<Temperature> findByThermometerId(Long thermometerId);

}

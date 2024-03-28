package org.temperature.controller;


import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.temperature.anomalies.AnomalyDetectionAlgorithm;
import org.temperature.model.TemperatureMeasurement;
import org.temperature.model.db.Room;
import org.temperature.model.db.Thermometer;
import org.temperature.repository.RoomRepository;
import org.temperature.repository.TemperatureRepository;
import org.temperature.repository.ThermometerRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class TemperatureController {

  @Autowired
  private TemperatureRepository temperatureRepository;

  @Autowired
  private ThermometerRepository thermometerRepository;

  @Autowired
  private AnomalyDetectionAlgorithm anomalyDetectionAlgorithm;
  @Autowired
  private RoomRepository roomRepository;


  @GetMapping("/")
  public ResponseEntity<String> index() {
    return new ResponseEntity<>("Greetings from Temperature Collector!", HttpStatus.OK);
  }

  @GetMapping("/thermometer/{identifier}/anomalies")
  public ResponseEntity<String> getAnomaliesForThermometer(
      @PathVariable("identifier") String identifier) {
    Thermometer found = thermometerRepository.findByIdentifier(identifier);
    if (found == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thermometer " + identifier + " not found");
    } else {
      List<TemperatureMeasurement> temperatureMeasurements = found.getTemperatures().stream()
          .map(x -> new TemperatureMeasurement(x.getTimestampMs(), x.getTemperature(),
              x.getThermometer().getIdentifier()))
          .collect(Collectors.toList());
      Set<TemperatureMeasurement> anomalies = anomalyDetectionAlgorithm.findAllAnomalies(
          temperatureMeasurements);
      String anomaliesString = anomalies.stream().collect(Collectors.toList()).toString();
      return new ResponseEntity<>(anomaliesString, HttpStatus.OK);
    }
  }

  @GetMapping("/room/{identifier}/anomalies")
  public ResponseEntity<String> getAnomaliesForRoom(@PathVariable("name") String identifier) {
    Room foundRoom = roomRepository.findByIdentifier(identifier);
    if(foundRoom == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room  " + identifier + " not found");
    }
    Set<TemperatureMeasurement> anomalies = new HashSet<>();
    for(Thermometer thermometer : foundRoom.getThermometers()) {
      List<TemperatureMeasurement> temperatureMeasurements = thermometer.getTemperatures().stream()
          .map(x -> new TemperatureMeasurement(x.getTimestampMs(), x.getTemperature(),
              x.getThermometer().getIdentifier()))
          .collect(Collectors.toList());
      anomalies.addAll( anomalyDetectionAlgorithm.findAllAnomalies(temperatureMeasurements));
    }
    String anomaliesString = anomalies.stream().collect(Collectors.toList()).toString();
    return new ResponseEntity<>(anomaliesString, HttpStatus.NOT_IMPLEMENTED);
  }

  @GetMapping("/anomalies/outliers")
  public ResponseEntity<String> getAnomalyOutliers() {
    //threshold static from properties
    return new ResponseEntity<>(
        "TODO : List of all thermometers w/ anomalies higher than threshold",
        HttpStatus.NOT_IMPLEMENTED);
  }

}

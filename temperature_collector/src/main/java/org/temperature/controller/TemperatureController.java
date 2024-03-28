package org.temperature.controller;


import java.util.ArrayList;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
  public ResponseEntity<List> getAnomaliesForThermometer(
      @PathVariable("identifier") String identifier) {
    Thermometer found = thermometerRepository.findByIdentifier(identifier);
    if (found == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thermometer " + identifier + " not found");
    } else {

      List<TemperatureMeasurement> temperatureMeasurements = found.getTemperatures().stream()
          .map(x -> new TemperatureMeasurement(x.getTimestampMs(), x.getTemperature(),
              x.getThermometer().getIdentifier()))
          .collect(Collectors.toList());

      return new ResponseEntity<>(new ArrayList<>(anomalyDetectionAlgorithm.findAllAnomalies(temperatureMeasurements)), HttpStatus.OK);
    }
  }

  @GetMapping("/room/{identifier}/anomalies")
  public ResponseEntity<List> getAnomaliesForRoom(@PathVariable("identifier") String identifier) {
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
    return new ResponseEntity<>(new ArrayList(anomalies), HttpStatus.OK);
  }

  @GetMapping("/anomalies/outliers")
  public ResponseEntity<List> getAnomalyOutliers() {
    Set<TemperatureMeasurement> anomalies = new HashSet<>();
    for(Room room: roomRepository.findAll()) {
      for(Thermometer thermometer : room.getThermometers()) {
        List<TemperatureMeasurement> temperatureMeasurements = thermometer.getTemperatures().stream()
            .map(x -> new TemperatureMeasurement(x.getTimestampMs(), x.getTemperature(),
                x.getThermometer().getIdentifier()))
            .collect(Collectors.toList());
        anomalies.addAll( anomalyDetectionAlgorithm.findAllAnomalies(temperatureMeasurements));
      }
    }
    return new ResponseEntity<>(new ArrayList(anomalies), HttpStatus.OK);
  }

}

package org.temperature.controller;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.temperature.model.AnomalyType;
import org.temperature.model.TemperatureMeasurement;
import org.temperature.model.db.Room;
import org.temperature.model.db.Temperature;
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
  private RoomRepository roomRepository;


  @GetMapping("/")
  public ResponseEntity<String> index() {
    return new ResponseEntity<>("Greetings from Temperature Collector!", HttpStatus.OK);
  }

  @GetMapping("/thermometer/{identifier}/anomalies/{algorithm}")
  public ResponseEntity<List> getAnomaliesForThermometer(
      @PathVariable("identifier") String identifier, @PathVariable("algorithm") String algorithm ) {
    AnomalyType anomalyType= determineAnomalyType(algorithm);
    if(anomalyType == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such algorithm " + algorithm );
    }
    Thermometer found = thermometerRepository.findByIdentifier(identifier);
    if (found == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thermometer " + identifier + " not found");
    } else {

      List<TemperatureMeasurement> temperatureMeasurements = found.getTemperatures().stream()
          .filter(x -> isOfAnomalyType(anomalyType,x ))
          .map(x -> new TemperatureMeasurement(x.getTimestampMs(), x.getTemperature(),
              x.getThermometer().getIdentifier()))
          .collect(Collectors.toList());

      return new ResponseEntity<>(temperatureMeasurements, HttpStatus.OK);
    }
  }

  @GetMapping("/room/{identifier}/anomalies/{algorithm}")
  public ResponseEntity<List> getAnomaliesForRoom(@PathVariable("identifier") String identifier, @PathVariable("algorithm") String algorithm ) {
    AnomalyType anomalyType= determineAnomalyType(algorithm);
    if(anomalyType == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such algorithm " + algorithm );
    }
    Room foundRoom = roomRepository.findByIdentifier(identifier);
    if(foundRoom == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room  " + identifier + " not found");
    }
    Set<TemperatureMeasurement> anomalies = new HashSet<>();
    for(Thermometer thermometer : foundRoom.getThermometers()) {
      List<TemperatureMeasurement> temperatureMeasurements = thermometer.getTemperatures().stream()
          .filter(x -> isOfAnomalyType(anomalyType,x ))
          .map(x -> new TemperatureMeasurement(x.getTimestampMs(), x.getTemperature(),
              x.getThermometer().getIdentifier()))
          .collect(Collectors.toList());
      anomalies.addAll(temperatureMeasurements);
    }
    return new ResponseEntity<>(new ArrayList(anomalies), HttpStatus.OK);
  }

  @GetMapping("/anomalies/{algorithm}")
  public ResponseEntity<List> getAnomalies(@PathVariable("algorithm") String algorithm) {
    AnomalyType anomalyType= determineAnomalyType(algorithm);
    if(anomalyType == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such algorithm " + algorithm );
    }
    Set<TemperatureMeasurement> anomalies = new HashSet<>();
    List<TemperatureMeasurement> temperatureMeasurements = StreamSupport.stream(temperatureRepository.findAll().spliterator(), false)
        .filter(x -> isOfAnomalyType(anomalyType, x))
        .map(x -> new TemperatureMeasurement(x.getTimestampMs(), x.getTemperature(),
            x.getThermometer().getIdentifier()))
        .collect(Collectors.toList());
    return new ResponseEntity<>(temperatureMeasurements, HttpStatus.OK);
  }

  private AnomalyType determineAnomalyType(String algorithm) {
    if("timesensitive".equalsIgnoreCase(algorithm)) {
      return AnomalyType.TIME_SENSITIVE;
    }else if("timeagnostic".equalsIgnoreCase(algorithm)){
      return AnomalyType.TIME_AGNOSTIC;
    }else {
      return null;
    }
  }

  private static boolean isOfAnomalyType(AnomalyType type, Temperature temperature) {
    switch(type) {
      case TIME_AGNOSTIC:
        return temperature.isTimeAgnosticAnomaly();
      case TIME_SENSITIVE:
        return temperature.isTimeSensitiveAnomaly();
      default:
        throw new NullPointerException();
    }
  }

}

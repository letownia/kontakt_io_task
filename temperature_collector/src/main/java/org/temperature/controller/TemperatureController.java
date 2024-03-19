package org.temperature.controller;


import org.springframework.web.bind.annotation.*;

@RestController
public class TemperatureController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Temperature Collector potential future coworker from Kontakt IO!";
    }

    @GetMapping("/thermomemeter/{therm_id}/anomalies")
    public String getAnomaliesForThermometer(@PathVariable("therm_id") String thermometerId) {
        return "TODO :List of all anomalies per thermometerId";
    }
    @GetMapping("/room/{room_id}/anomalies")
    public String getAnomaliesForRoom(@PathVariable("room_id") String roomId) {
        return "TODO : List of all anomalies by roomId";
    }

    @GetMapping("/anomalies/outliers")
    public String getAnomalyOutliers() {
        //threshold static from properties
        return "TODO : List of all thermometers w/ anomalies higher than threshold";
    }

}

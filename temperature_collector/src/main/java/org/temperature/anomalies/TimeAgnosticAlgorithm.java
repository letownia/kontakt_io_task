package org.temperature.anomalies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.temperature.config.KafkaConfig;
import org.temperature.model.TemperatureMeasurement;

import java.util.*;
import java.util.stream.Collectors;

public class TimeAgnosticAlgorithm implements AnomalyDetectionAlgorithm {
    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    private static final int MEAN_WINDOW_SIZE = 10;
    private static final double OUTLIER_THRESHOLD = 5.0;

    @Override
    public Set<TemperatureMeasurement> findAllAnomalies(List<TemperatureMeasurement> temperatureMeasurements) {
        if (temperatureMeasurements.size() < MEAN_WINDOW_SIZE) {
            log.warn("No anomalies can be detected with less than 10 measurements");
            return Collections.EMPTY_SET;
        }
        Set<TemperatureMeasurement> foundAnomalies = new HashSet<>();

        for(int i = 0; i <= temperatureMeasurements.size() - MEAN_WINDOW_SIZE; i ++) {
            double averageTemp = temperatureMeasurements.stream().skip(i).limit(MEAN_WINDOW_SIZE).mapToDouble(x -> x.temperature()).average().getAsDouble();
            Set<TemperatureMeasurement> newAnomalies =
                    temperatureMeasurements.stream().skip(i).limit(MEAN_WINDOW_SIZE)
                            .filter(x -> Math.abs(x.temperature() - averageTemp) >= OUTLIER_THRESHOLD).collect(Collectors.toSet());
            log.info("Found new anomalies" + foundAnomalies);
            foundAnomalies.addAll(newAnomalies);
        }
        return foundAnomalies;
    }
}

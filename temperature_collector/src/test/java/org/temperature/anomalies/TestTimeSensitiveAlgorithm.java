package org.temperature.anomalies;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.temperature.anomalies.TimeAgnosticAlgorithm.MEAN_WINDOW_SIZE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import org.temperature.model.TemperatureMeasurement;

public class TestTimeSensitiveAlgorithm {

  private final static int DEFAULT_WINDOW_SECONDS = 10;
  private final static String THERMOMETER_NAME ="test_1";
  @Test
  public void test() {
    TimeSensitiveAlgorithm algorithm = new TimeSensitiveAlgorithm(DEFAULT_WINDOW_SECONDS);
    double normalTemp = 20.0;
    double outlierTemp = 40.0;
    long timestamp = 1000;

    List<TemperatureMeasurement> measurements =
        Stream.generate( () -> new TemperatureMeasurement(100l, normalTemp, THERMOMETER_NAME)).limit(10).collect(
        Collectors.toList());
    TemperatureMeasurement anomaly = new TemperatureMeasurement(100l, outlierTemp, THERMOMETER_NAME);
    measurements.add(anomaly);
    Set<TemperatureMeasurement> anomalies = algorithm.findAllAnomalies(measurements);
    assertTrue(anomalies.size() == 1 && anomalies.iterator().next().temperature()== outlierTemp);
  }
}

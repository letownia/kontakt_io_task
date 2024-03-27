package org.temperature.anomalies;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.temperature.anomalies.TimeAgnosticAlgorithm.MEAN_WINDOW_SIZE;
import static org.temperature.anomalies.TimeSensitiveAlgorithm.OUTLIER_THRESHOLD_TEMPERATURE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import org.temperature.model.TemperatureMeasurement;

public class TestTimeSensitiveAlgorithm {

  private static Function<Double,TemperatureMeasurement> generateSpecificTemperature
      = x -> new TemperatureMeasurement(getMockTimeMs(), x,"therm_1");
  private static Supplier<TemperatureMeasurement> generateTwentyDegreeTemperature =
      () -> generateSpecificTemperature.apply(20.0);
  private static Supplier<TemperatureMeasurement> generateRandomTemperature =
      () -> generateSpecificTemperature.apply(Math.random());

  private final static int DEFAULT_WINDOW_SIZE_MS = 10*1000;
  private final static String THERMOMETER_NAME ="test_1";

  //If these tests are ever to be run in parallel mockTimeMs cannot be static.
  private static long mockTimeMs = 1000l;
  private static long getMockTimeMs() {
    return mockTimeMs += 1000l;
  }

  @Test
  public void testOneAnamoly() {
    TimeSensitiveAlgorithm algorithm = new TimeSensitiveAlgorithm(DEFAULT_WINDOW_SIZE_MS);
    double outlierTemp = 20.0 + OUTLIER_THRESHOLD_TEMPERATURE*2;

    List<TemperatureMeasurement> measurements =
        Stream.generate(generateTwentyDegreeTemperature).limit(9).collect(
        Collectors.toList());
    TemperatureMeasurement anomaly = new TemperatureMeasurement(getMockTimeMs(), outlierTemp, THERMOMETER_NAME);
    measurements.add(anomaly);
    Set<TemperatureMeasurement> anomalies = algorithm.findAllAnomalies(measurements);
    assertTrue(anomalies.size() == 1 && anomalies.iterator().next().temperature()== outlierTemp);
  }

  @Test
  public void testTooMuchTimePassed() {
    long sleepExtraMs = DEFAULT_WINDOW_SIZE_MS;
    TimeSensitiveAlgorithm algorithm = new TimeSensitiveAlgorithm(DEFAULT_WINDOW_SIZE_MS);
    double outlierTemp = 20.0 + OUTLIER_THRESHOLD_TEMPERATURE*2;

    List<TemperatureMeasurement> measurements =
        Stream.generate(generateTwentyDegreeTemperature).limit(9).collect(
            Collectors.toList());
    TemperatureMeasurement anomaly = new TemperatureMeasurement(getMockTimeMs() +  sleepExtraMs, outlierTemp, THERMOMETER_NAME);
    measurements.add(anomaly);
    Set<TemperatureMeasurement> anomalies = algorithm.findAllAnomalies(measurements);
    assertTrue(anomalies.size() == 0);
  }
}

package org.temperature.anomalies;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.temperature.anomalies.TimeAgnosticAlgorithm.OUTLIER_THRESHOLD_TEMPERATURE;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.temperature.model.TemperatureMeasurement;

@Deprecated
public class TestDeprecatedTimeAgnosticAlgorithm {

  private final int MEAN_WINDOW_SIZE = 10;
  //warning: nanoTime() is used rather than currentTimeMillis() in order to ensure each generated record is unique
  private static Function<Double,TemperatureMeasurement> generateSpecificTemperature = x -> new TemperatureMeasurement(System.nanoTime(), x,"therm_1");
  private static Supplier<TemperatureMeasurement> generateTwentyDegreeTemperature = () -> generateSpecificTemperature.apply(20.0);
  private static Supplier<TemperatureMeasurement> generateRandomTemperature = () -> generateSpecificTemperature.apply(Math.random());

  private TimeAgnosticAlgorithm timeAgnosticAlgorithm;

  @Before
  public void init() {
    timeAgnosticAlgorithm = new TimeAgnosticAlgorithm(MEAN_WINDOW_SIZE);
  }


  @Test
  public void testTooFewElements() {
    List<TemperatureMeasurement> measurements = Collections.nCopies(MEAN_WINDOW_SIZE - 1,
        new TemperatureMeasurement(1000l, 20.0, "therm_1"));
    Set<TemperatureMeasurement> anomalies = timeAgnosticAlgorithm.findAllAnomalies(measurements);
    assertTrue(anomalies.isEmpty());
  }
  @Test
  public void testEmptyList() {
    Set<TemperatureMeasurement> anomalies = timeAgnosticAlgorithm.findAllAnomalies(Collections.emptyList());
    assertTrue(anomalies.isEmpty());
  }

  @Test
  public void testNoAnomalies() {
    List<TemperatureMeasurement> tenAnomalies= Stream.generate(generateTwentyDegreeTemperature).limit(MEAN_WINDOW_SIZE).collect(
        Collectors.toList());
    Set<TemperatureMeasurement> anomalies = timeAgnosticAlgorithm.findAllAnomalies(tenAnomalies);

    assertTrue(anomalies.isEmpty());
  }

  @Test
  public void test1Anomaly() {
    List<TemperatureMeasurement> measurements = Stream.generate(generateTwentyDegreeTemperature).limit(MEAN_WINDOW_SIZE).collect(
        Collectors.toList());
    TemperatureMeasurement anomaly = new TemperatureMeasurement(1000l, measurements.get(0)
        .temperature() + OUTLIER_THRESHOLD_TEMPERATURE*2, "therm_1");

    measurements.set(MEAN_WINDOW_SIZE-1, anomaly);

    Set<TemperatureMeasurement> anomalies = timeAgnosticAlgorithm.findAllAnomalies(measurements);
    assertTrue(anomalies.size() == 1);
  }


  @Test
  public void testManyAnomalies() throws InterruptedException {
    List<TemperatureMeasurement> measurements = Stream.generate(generateTwentyDegreeTemperature).limit(MEAN_WINDOW_SIZE*5).collect(
        Collectors.toList());
    Double outlierTemperature= 20.0  + OUTLIER_THRESHOLD_TEMPERATURE*2;
    measurements.set(MEAN_WINDOW_SIZE -1, generateSpecificTemperature.apply(outlierTemperature));
    measurements.set(MEAN_WINDOW_SIZE*2 -1, generateSpecificTemperature.apply(outlierTemperature));
    measurements.set(MEAN_WINDOW_SIZE*3 -1, generateSpecificTemperature.apply(outlierTemperature));
    measurements.set(MEAN_WINDOW_SIZE*4 -1, generateSpecificTemperature.apply(outlierTemperature));
    measurements.set(MEAN_WINDOW_SIZE*5 -1, generateSpecificTemperature.apply(outlierTemperature));

    Set<TemperatureMeasurement> anomalies = timeAgnosticAlgorithm.findAllAnomalies(measurements);
    assertTrue(anomalies.size() == 5);
  }



}

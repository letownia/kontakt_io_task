package org.temperature.anomalies;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.temperature.anomalies.TimeAgnosticAlgorithm.MEAN_WINDOW_SIZE;
import static org.temperature.anomalies.TimeAgnosticAlgorithm.OUTLIER_THRESHOLD_TEMPERATURE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.temperature.model.TemperatureMeasurement;

public class TestTimeAgnosticAlgorithm {


  @Test
  public void testTooFewElements() {
    TimeAgnosticAlgorithm timeAgnosticAlgorithm = new TimeAgnosticAlgorithm();
    List<TemperatureMeasurement> measurements = Collections.nCopies(MEAN_WINDOW_SIZE - 1,
        new TemperatureMeasurement(1000l, 20.0, "therm_1"));
    Set<TemperatureMeasurement> anomalies = timeAgnosticAlgorithm.findAllAnomalies(measurements);
    assertTrue(anomalies.isEmpty());
  }
  @Test
  public void testEmptyList() {
    TimeAgnosticAlgorithm timeAgnosticAlgorithm = new TimeAgnosticAlgorithm();
    Set<TemperatureMeasurement> anomalies = timeAgnosticAlgorithm.findAllAnomalies(Collections.emptyList());
    assertTrue(anomalies.isEmpty());
  }

  @Test
  public void test1Anomaly() {
    TimeAgnosticAlgorithm timeAgnosticAlgorithm = new TimeAgnosticAlgorithm();
    List<TemperatureMeasurement> nCopiesList = Collections.nCopies(MEAN_WINDOW_SIZE - 1,
        new TemperatureMeasurement(1000l, 20.0, "therm_1"));

    System.out.println("Measurement first " + nCopiesList.get(0));
    TemperatureMeasurement anomaly = new TemperatureMeasurement(1000l, nCopiesList.get(0)
        .temperature() + OUTLIER_THRESHOLD_TEMPERATURE*2, "therm_1");

    List<TemperatureMeasurement> measurements = new ArrayList<TemperatureMeasurement>(nCopiesList);
    measurements.add(anomaly);

    Set<TemperatureMeasurement> anomalies = timeAgnosticAlgorithm.findAllAnomalies(measurements);
    assertTrue(anomalies.size() == 1);
  }

}

package org.temperature.anomalies;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.temperature.model.db.Temperature;
import org.temperature.model.db.Thermometer;
import org.temperature.repository.TemperatureRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestTimeAgnosticAlgorithm {
  private static final int TEST_WINDOW_SIZE = 10;
  @Mock
  private static Thermometer defaultThermometer;
  private static Function<Double, Temperature> generateSpecificTemperature =
      (x) -> { Temperature t = new Temperature();
               t.setTemperature(x);
               t.setTimestampMs(System.nanoTime());
               t.setThermometer(defaultThermometer);
    return t;
  };

  private static Supplier<Temperature> generateTwentyDegreeTemperature = () -> generateSpecificTemperature.apply(20.0);
  private static Supplier<Temperature> generate0to20DegreeTemperature = () -> generateSpecificTemperature.apply(Math.random()*20);

  @Mock
  private TemperatureRepository temperatureRepository;
  @InjectMocks
  private TimeAgnosticAlgorithm timeAgnosticAlgorithm;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    timeAgnosticAlgorithm = new TimeAgnosticAlgorithm(TEST_WINDOW_SIZE);
    when(defaultThermometer.getId()).thenReturn(1L);
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testSetup() {
    assert (timeAgnosticAlgorithm != null);
    assert (temperatureRepository != null);
  }

  @Test
  public void test0Anomalies() {
    when(temperatureRepository.getXPreviousTemperatures(anyInt(), anyLong(), anyLong())).thenReturn(
        Stream.generate(generateTwentyDegreeTemperature).limit(TEST_WINDOW_SIZE).collect(
            Collectors.toList()));
    Temperature newTemperature = generateTwentyDegreeTemperature.get();

    assert(timeAgnosticAlgorithm.findNewAnomaliesForNewTemperature(newTemperature).isEmpty());
  }

  @Test
  public void test1Anomalies() {

    List<Temperature> temperatures =  Stream.generate(generateTwentyDegreeTemperature).limit(TEST_WINDOW_SIZE).collect(Collectors.toList());
    temperatures.add(generateSpecificTemperature.apply(20.0 * 3));

    when(temperatureRepository.getXPreviousTemperatures(anyInt(), anyLong(), anyLong())).thenReturn(temperatures);
    Temperature newTemperature = generateTwentyDegreeTemperature.get();

    assert(timeAgnosticAlgorithm.findNewAnomaliesForNewTemperature(newTemperature).size() == 1);
  }

  @Test
  public void testManyAnomalies() {

    List<Temperature> temperatures =
        Stream.generate(generate0to20DegreeTemperature).limit(TEST_WINDOW_SIZE*50).collect(Collectors.toList());

    when(temperatureRepository.getXPreviousTemperatures(anyInt(), anyLong(), anyLong())).thenReturn(temperatures);
    Temperature newTemperature = generateTwentyDegreeTemperature.get();

    assert(timeAgnosticAlgorithm.findNewAnomaliesForNewTemperature(newTemperature).size() >= 1);
  }


}

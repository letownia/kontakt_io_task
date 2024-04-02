package org.temperature.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.temperature.anomalies.AnomalyDetectionAlgorithm;
import org.temperature.anomalies.TimeAgnosticAlgorithm;
import org.temperature.anomalies.TimeSensitiveAlgorithm;

@Configuration
public class CollectorConfig {

  @Autowired
  private Environment environment;

//
//  @Bean TimeAgnosticAlgorithm
//  @Bean
//  public AnomalyDetectionAlgorithm getAnomalyDetectionAlgorithm() {
//    String algorithm = environment.getProperty("detection.algorithm", "TimeAgnostic");
//    if (algorithm.equalsIgnoreCase("timeAgnostic")) {
//      return new TimeAgnosticAlgorithm();
//    } else if (algorithm.equalsIgnoreCase("timeSensitive")) {
//      return new TimeSensitiveAlgorithm(10*1000);
//    } else {
//      throw new RuntimeException(
//          "Invalid configuration - incorrect algorithm, see : detection.algorithm : "
//              + algorithm);
//    }
//  }

  @Bean
  public TimeSensitiveAlgorithm getTimeSensitiveAlgorithm() {
    return new TimeSensitiveAlgorithm(10*1000);
  }

  @Bean
  public TimeAgnosticAlgorithm getTimeAgnosticAlgorithm() {
    return new TimeAgnosticAlgorithm(10);
  }
}

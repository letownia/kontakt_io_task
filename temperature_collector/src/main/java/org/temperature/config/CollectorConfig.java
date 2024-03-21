package org.temperature.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.temperature.anomalies.AnomalyDetectionAlgorithm;
import org.temperature.anomalies.TimeAgnosticAlgorithm;

@Configuration
public class CollectorConfig {

    @Autowired
    private Environment environment;

    @Bean
    public AnomalyDetectionAlgorithm getAnomalyDetectionAlgorithm() {
        String algorithm = environment.getProperty("detection.algorithm", "TimeAgnostic");
        if(algorithm.equalsIgnoreCase("TimeAgnostic")) {
            return new TimeAgnosticAlgorithm();
        }else {
            throw new RuntimeException("Invalid configuration - incorrect algorithm, see : detection.algorithm : "
                    + environment.getProperty("detection.algorithm"));
        }
    }
}

package org.temperature;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.temperature.repository.RoomRepository;
import org.temperature.repository.TemperatureRepository;
import org.temperature.repository.ThermometerRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestRestController {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ThermometerRepository thermometerRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void exampleTest() {
        String body = this.restTemplate.getForObject("/", String.class);
        assertThat(body).contains("Greetings");
    }
}
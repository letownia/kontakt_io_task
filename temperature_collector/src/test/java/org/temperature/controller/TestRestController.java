package org.temperature.controller;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.temperature.repository.RoomRepository;
import org.temperature.repository.TemperatureRepository;
import org.temperature.repository.ThermometerRepository;

import static org.assertj.core.api.Assertions.assertThat;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestRestController {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private TemperatureRepository temperatureRepository;

  @MockBean
  private RoomRepository roomRepository;

  @MockBean
  private ThermometerRepository thermometerRepository;

  @Test
  public void exampleTest() {
    String body = this.restTemplate.getForObject("/", String.class);
    assertThat(body).contains("Greetings");
  }

}
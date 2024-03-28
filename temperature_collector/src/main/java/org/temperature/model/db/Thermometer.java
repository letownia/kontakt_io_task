package org.temperature.model.db;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Thermometer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String identifier;
  @ManyToOne
  @JoinColumn(name = "room_id")
  private Room room;
  @OneToMany(mappedBy = "thermometer")
  private Set<Temperature> temperatures = new HashSet<>();

  public Room getRoom() {
    return room;
  }

  public String getIdentifier() {
    return identifier;
  }

  public Long getId() {
    return id;
  }

  public Set<Temperature> getTemperatures() {
    return temperatures;
  }
}
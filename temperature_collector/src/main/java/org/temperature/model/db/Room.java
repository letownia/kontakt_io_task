package org.temperature.model.db;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "room")
    private Set<Thermometer> thermometers = new HashSet<>();
    @Column(unique=true)
    private String roomName;
    public String getRoomName() {
        return roomName;
    }
    public Set<Thermometer> getThermometers() {
        return thermometers;
    }

    public Long getId() {
        return id;
    }

}
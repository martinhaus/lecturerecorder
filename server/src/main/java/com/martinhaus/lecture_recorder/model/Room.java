package com.martinhaus.lecture_recorder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="rooms")
public class Room {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String ipAdress;
    private String audioSource;

    public Room(String name, String ipAdress, String audioSource) {

        this.name = name;
        this.ipAdress = ipAdress;
        this.audioSource = audioSource;
    }
}

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
    private String ipAddress;
    private String audioSource;
    private String timetableId;
    private String timetableRoomName;

    public Room(String name, String ipAddress, String audioSource) {

        this.name = name;
        this.ipAddress = ipAddress;
        this.audioSource = audioSource;
    }
}

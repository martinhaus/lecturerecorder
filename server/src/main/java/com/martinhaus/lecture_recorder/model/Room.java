package com.martinhaus.lecture_recorder.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data @NoArgsConstructor
@Table(name="rooms")
public class Room {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String ipAdress;
    private String audioSource;

    @OneToMany(mappedBy="room")
    private List<Recording> recordings;

    public Room(String name, String ipAdress, String audioSource) {

        this.name = name;
        this.ipAdress = ipAdress;
        this.audioSource = audioSource;
    }


    public Room(String name, String ipAdress, String audioSource, List<Recording> recordings) {
        this.name = name;
        this.ipAdress = ipAdress;
        this.audioSource = audioSource;
        this.recordings = recordings;
    }
}

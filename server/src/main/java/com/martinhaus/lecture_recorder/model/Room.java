package com.martinhaus.lecture_recorder.model;

import javax.persistence.*;
import java.util.List;

@Entity
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

    public Room() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public String getAudioSource() {
        return audioSource;
    }

    public void setAudioSource(String audioSource) {
        this.audioSource = audioSource;
    }
}

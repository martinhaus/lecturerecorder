package com.martinhaus.lecture_recorder.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "recording_downloads")
public class RecordingDownload {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Recording recording;
    private String uuid;

    public RecordingDownload(Recording recording) {
        this.recording = recording;
        this.uuid = UUID.randomUUID().toString();
    }

}

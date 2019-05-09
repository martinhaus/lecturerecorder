package com.martinhaus.lecture_recorder.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recording_downloads")
public class RecordingDownload {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recording_id")
    private Recording recording;
    private String uuid;

    public RecordingDownload(Recording recording) {
        this.recording = recording;
        this.uuid = UUID.randomUUID().toString();
    }

}

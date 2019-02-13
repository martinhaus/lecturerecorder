package com.martinhaus.lecture_recorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="recordings")
public class Recording {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @NotNull
    private String title;
    @CreationTimestamp
    private Date datetimeCreated;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
    @ColumnDefault("false")
    private boolean error;
    @ColumnDefault("false")
    private boolean finished;
    @ColumnDefault("false")
    private boolean active;

    private long pid;

    @Nullable
    private String fileName;

    @ManyToOne
    @JoinColumn(name="room_id", nullable=false)
    private Room room;

    @OneToMany(mappedBy = "recording", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RecordingDownload> downloadSet;

    public Recording(String title, Date datetimeCreated, LocalDateTime startTime, LocalDateTime endTime, boolean error, boolean finished, Room room) {
        this.title = title;
        this.datetimeCreated = datetimeCreated;
        this.startTime = startTime;
        this.endTime = endTime;
        this.error = error;
        this.finished = finished;
        this.room = room;
    }

    public Recording(String title, LocalDateTime startTime, LocalDateTime endTime, boolean error, boolean finished, Room room) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.error = error;
        this.finished = finished;
        this.room = room;
    }
}

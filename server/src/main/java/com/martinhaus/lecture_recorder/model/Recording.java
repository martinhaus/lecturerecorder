package com.martinhaus.lecture_recorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
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
    @ColumnDefault("NULL")
    private String email;


    private long pid;

    @Nullable
    private String fileName;

    @ManyToOne
    @JoinColumn(name="room_id", nullable=false)
    private Room room;

    @OneToMany(mappedBy = "recording", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RecordingDownload> downloadSet;

    public Recording(String title, Date datetimeCreated, LocalDateTime startTime, LocalDateTime endTime, boolean error, boolean finished, Room room, String email) {
        this.title = title;
        this.datetimeCreated = datetimeCreated;
        this.startTime = startTime.withNano(0).withSecond(0);
        this.endTime = endTime;
        this.error = error;
        this.finished = finished;
        this.room = room;
        this.email = email;
    }

    public Recording(String title, LocalDateTime startTime, LocalDateTime endTime, boolean error, boolean finished, Room room) {
        this.title = title;
        this.startTime = startTime.withNano(0).withSecond(0);
        this.endTime = endTime;
        this.error = error;
        this.finished = finished;
        this.room = room;
    }
}

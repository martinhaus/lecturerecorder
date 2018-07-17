package com.martinhaus.lecture_recorder.model;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="recordings")
public class Recording {

    //TODO dat deafult false pre boolean

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

    @ManyToOne
    @JoinColumn(name="room_id", nullable=false)
    private Room room;

    public Recording() {
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(Date datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}

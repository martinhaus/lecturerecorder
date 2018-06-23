package com.martinhaus.lecture_recorder.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="recordings")
public class Recording {

    //TODO dat deafult false pre boolean

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String title;
    private Date datetimeCreated;
    private Date datetimeScheduled;
    private Date startTime;
    private Date endTime;
    private boolean error;
    private boolean finished;

    @ManyToOne
    @JoinColumn(name="room_id", nullable=false)
    Room room;

    public Recording() {
    }

    public Recording(String title, Date datetimeCreated, Date datetimeScheduled, Date startTime, Date endTime, boolean error, boolean finished, Room room) {
        this.title = title;
        this.datetimeCreated = datetimeCreated;
        this.datetimeScheduled = datetimeScheduled;
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

    public Date getDatetimeScheduled() {
        return datetimeScheduled;
    }

    public void setDatetimeScheduled(Date datetimeScheduled) {
        this.datetimeScheduled = datetimeScheduled;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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
}

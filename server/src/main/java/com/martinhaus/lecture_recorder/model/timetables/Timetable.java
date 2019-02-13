package com.martinhaus.lecture_recorder.model.timetables;

import com.martinhaus.lecture_recorder.model.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="timetables")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    String termTitle;

    @CreationTimestamp
    private Date creationDate;

    @UpdateTimestamp
    private Date updateDate;

    @OneToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Lesson> lessonsList;
}

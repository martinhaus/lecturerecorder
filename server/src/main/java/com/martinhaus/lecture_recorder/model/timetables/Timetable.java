package com.martinhaus.lecture_recorder.model.timetables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.martinhaus.lecture_recorder.model.Room;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
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

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

//    @JsonIgnore
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Lesson> lessonsList;
}

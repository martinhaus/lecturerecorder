package com.martinhaus.lecture_recorder.model.timetables;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="lessons")
class Lesson {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    private String teacher;

    @NotNull
    private short dayOfWeek;
}

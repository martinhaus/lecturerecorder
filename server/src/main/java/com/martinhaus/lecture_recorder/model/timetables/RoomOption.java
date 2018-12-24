package com.martinhaus.lecture_recorder.model.timetables;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomOption {
    Short id;
    String title;
}

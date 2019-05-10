package com.martinhaus.lecture_recorder.controllers;

import com.martinhaus.lecture_recorder.model.timetables.Timetable;
import com.martinhaus.lecture_recorder.services.TimetableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimetableController.class)
@ExtendWith(SpringExtension.class)
class TimetableControllerTest {

    @MockBean
    private TimetableService timetableService;

    @Autowired
    private MockMvc mvc;

    /**
     * Unit testing for Recording Rest Controller
     * Product requirement: G-6 Recording lectures from timetables
     */
    @Test
    void getByRoomId_returnNormalTimetable() throws Exception {
        given(timetableService.getByRoomId(anyLong())).willReturn(new Timetable());
        this.mvc.perform(get("/room/1/timetable")).andExpect(status().isOk());
    }


    /**
     * Unit testing for Recording Rest Controller
     * Product requirement: G-6 Recording lectures from timetables
     */
    @Test
    void getByRoomId_returnNull() throws Exception {
        given(timetableService.getByRoomId(anyLong())).willReturn(null);
        this.mvc.perform(get("/room/1/timetable")).andExpect(status().is4xxClientError());
    }
}
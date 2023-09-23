package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.HitResponse;
import ru.practicum.dto.ViewStatsResponse;
import ru.practicum.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatisticController.class)
public class StatisticControllerTests {

    @Autowired
    ObjectMapper mapper = new ObjectMapper();

    @MockBean
    StatisticService service;

    @Autowired
    private MockMvc mvc;

    HitResponse response1;
    HitRequest request1;
    ViewStatsResponse statsResponse;

    @BeforeEach
    void setUp() {
        response1 = new HitResponse("app", "uri", "ip", LocalDateTime.now());
        request1 = new HitRequest("app", "uri", "ip", LocalDateTime.now().toString());
        statsResponse = new ViewStatsResponse("app", "uri", 1L);
    }

    @Test
    void saveHit() throws Exception {
        when(service.saveHit(any())).thenReturn(response1);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(request1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(mapper.writeValueAsString(response1)));
    }

    @Test
    void getStats() throws Exception {
        when(service.getStats(anyString(), anyString(), any(), anyBoolean())).thenReturn(List.of(statsResponse));

        mvc.perform(get("/stats")
                        .param("start", "2023-03-03 00:00:00")
                        .param("end", "2023-03-03 00:00:00")
                        .param("uri", "uri"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(statsResponse))));
    }
}

package ru.practicum.server;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.ViewStatsResponse;
import ru.practicum.service.StatisticService;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(
        properties = "db.name=test3",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatisticServiceTests {

    private final StatisticService service;

    HitRequest hit1;
    HitRequest hit2;
    HitRequest hit3;
    HitRequest hit4;
    List<ViewStatsResponse> statsTest1 = new ArrayList<>(List.of(new ViewStatsResponse("app", "uri", 1L)));
    List<ViewStatsResponse> statsTest2 = new ArrayList<>(List.of(new ViewStatsResponse("app", "anotherUri", 1L)));
    List<ViewStatsResponse> statsTest3 = new ArrayList<>(List.of(new ViewStatsResponse("app", "anotherUri", 2L),
            new ViewStatsResponse("app", "uri", 2L)));
    List<ViewStatsResponse> statsTest4 = new ArrayList<>(List.of(new ViewStatsResponse("app", "uri", 2L),
            new ViewStatsResponse("app", "anotherUri", 1L)));

    @BeforeEach
    void setUp() {
        hit1 = new HitRequest("app", "uri", "001.001.001.001", "2023-01-01 00:00:00");
        hit2 = new HitRequest("app", "uri", "002.002.002.002", "2023-03-03 00:00:00");
        hit3 = new HitRequest("app", "anotherUri", "003.003.003.003", "2023-05-05 00:00:00");
        hit4 = new HitRequest("app", "anotherUri", "003.003.003.003", "2023-07-07 00:00:00");
    }

    @Test
    void saveHitAndGetStatsTest() {
        service.saveHit(hit1);
        service.saveHit(hit2);
        service.saveHit(hit3);
        service.saveHit(hit4);
        List<ViewStatsResponse> statsByUri = service.getStats("2023-03-03 00:00:00", "2023-07-07 00:00:00",
                List.of("uri"), false);
        List<ViewStatsResponse> statsByUriUniq = service.getStats("2023-01-01 00:00:00", "2023-07-07 00:00:00",
                List.of("anotherUri"), true);
        List<ViewStatsResponse> stats = service.getStats("2023-01-01 00:00:00", "2023-07-07 00:00:00",
                null, false);
        List<ViewStatsResponse> statsUniq = service.getStats("2023-01-01 00:00:00", "2023-07-07 00:00:00",
                null, true);

        Assertions.assertEquals(statsTest1, statsByUri);
        Assertions.assertEquals(statsTest2, statsByUriUniq);
        Assertions.assertEquals(statsTest3, stats);
        Assertions.assertEquals(statsTest4, statsUniq);
    }
}

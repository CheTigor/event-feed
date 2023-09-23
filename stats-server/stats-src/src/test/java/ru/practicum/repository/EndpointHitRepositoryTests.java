package ru.practicum.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EndpointHitRepositoryTests {

    @Autowired
    private EndpointHitRepository repository;

    EndpointHit hit1;
    EndpointHit hit2;
    EndpointHit hit3;
    EndpointHit hit4;
    LocalDateTime one;
    LocalDateTime three;
    LocalDateTime five;
    LocalDateTime seven;

    @BeforeEach
    void setUp() {
        one = LocalDateTime.of(2023, 1, 1, 1, 1, 1);
        three = LocalDateTime.of(2023, 3, 3, 3, 3, 3);
        five = LocalDateTime.of(2023, 5, 5, 5, 5, 5);
        seven = LocalDateTime.of(2023, 7, 7, 7, 7, 7);
        hit1 = new EndpointHit(null, "app", "uri", "184.245.123.001", one);
        hit2 = new EndpointHit(null, "app", "anotherUri", "192.001.1.1", three);
        hit3 = new EndpointHit(null, "app", "anotherUri", "192.001.1.1", five);
        hit4 = new EndpointHit(null, "app", "uri", "251.194.001.001", seven);

        repository.save(hit1);
        repository.save(hit2);
        repository.save(hit3);
        repository.save(hit4);
    }

    @Test
    void getStatsByUriUniqTest() {
        List<ViewStats> stats = repository.getStatsByUriUniq(List.of("anotherUri", "uri"), one, seven);
        ViewStats forUri = new ViewStats("app", "uri", 2L);
        ViewStats forAnotherUri = new ViewStats("app", "anotherUri", 1L);

        Assertions.assertEquals(List.of(forUri, forAnotherUri), stats);
    }

    @Test
    void getStatsTest() {
        List<ViewStats> stats = repository.getStats(three, seven);
        ViewStats forUri = new ViewStats("app", "uri", 1L);
        ViewStats forAnotherUri = new ViewStats("app", "anotherUri", 2L);

        Assertions.assertEquals(List.of(forAnotherUri, forUri), stats);
    }

    @Test
    void getStatsUniqTest() {
        List<ViewStats> stats = repository.getStatsUniq(one, five);
        ViewStats forUri = new ViewStats("app", "uri", 1L);
        ViewStats forAnotherUri = new ViewStats("app", "anotherUri", 1L);

        Assertions.assertEquals(List.of(forAnotherUri, forUri), stats);
    }

    @Test
    void getStatsByUriTest() {
        List<ViewStats> stats = repository.getStatsByUriUniq(List.of("anotherUri"), one, five);
        ViewStats forAnotherUri = new ViewStats("app", "anotherUri", 1L);

        Assertions.assertEquals(List.of(forAnotherUri), stats);
    }


}

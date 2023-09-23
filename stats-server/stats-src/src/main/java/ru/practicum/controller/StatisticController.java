package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.HitResponse;
import ru.practicum.dto.ViewStatsResponse;
import ru.practicum.service.StatisticService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@Valid
public class StatisticController {

    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitResponse saveHit(@RequestBody @Valid HitRequest hit) {
        log.info("Получен запрос POST saveHit: hitRequest = \n{}", hit);
        final HitResponse savedHit = statisticService.saveHit(hit);
        log.info("Получен ответ POST saveHit: hitResponse = \n{}", savedHit);
        return savedHit;
    }

    @GetMapping("/stats")
    public List<ViewStatsResponse> getStats(@RequestParam("start") String start,
                                            @RequestParam("end") String end,
                                            @RequestParam(value = "uris", required = false) List<String> uri,
                                            @RequestParam(value = "unique", defaultValue = "false") Boolean unique) {
        log.info("Получен запрос GET getStats: \nstart = {}, \nend = {}, \nuri = {}, \nunique = {}", start, end, uri,
                unique);
        final List<ViewStatsResponse> stats = statisticService.getStats(start, end, uri, unique);
        log.info("Получен ответ GET getStats: stats = \n{}", stats);
        return stats;
    }

}

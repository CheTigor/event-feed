package ru.practicum.service;

import ru.practicum.dto.HitRequest;
import ru.practicum.dto.HitResponse;
import ru.practicum.dto.ViewStatsResponse;

import java.util.List;

public interface StatisticService {

    List<ViewStatsResponse> getStats(String start, String end, List<String> uri, Boolean unique);

    HitResponse saveHit(HitRequest hit);
}

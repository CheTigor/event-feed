package ru.practicum.mapper;

import ru.practicum.constants.Constants;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.HitResponse;
import ru.practicum.dto.ViewStatsResponse;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;

public class HitMapper {

    public static EndpointHit toEndpointHit(HitRequest hit) {
        return new EndpointHit(null, hit.getApp(), hit.getUri(), hit.getIp(), LocalDateTime.parse(hit.getTimestamp(),
                Constants.DATE_TIME_FORMAT));
    }

    public static ViewStatsResponse toViewStatsResponse(ViewStats stats) {
        return new ViewStatsResponse(stats.getApp(), stats.getUri(), stats.getHits());
    }

    public static HitResponse toHitResponse(EndpointHit hit) {
        return new HitResponse(hit.getApp(), hit.getUri(), hit.getIp(), hit.getCreated());
    }
}

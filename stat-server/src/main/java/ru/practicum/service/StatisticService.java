package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.HitResponse;
import ru.practicum.dto.ViewStatsResponse;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.EndPointHitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatisticService {

    private final EndPointHitRepository endPointHitRepository;
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatisticService(EndPointHitRepository endPointHitRepository) {
        this.endPointHitRepository = endPointHitRepository;
    }

    public List<ViewStatsResponse> getStats(String start, String end, List<String> uri, Boolean unique) {
        LocalDateTime startTime;
        LocalDateTime endTime;
        startTime = LocalDateTime.parse(start, dateTimeFormatter);
        endTime = LocalDateTime.parse(end, dateTimeFormatter);
        if (unique && uri != null) {
            return endPointHitRepository.getStatsByUriUniq(uri, startTime, endTime).stream().map(HitMapper::toViewStatsResponse)
                    .collect(Collectors.toList());
        } else if (unique && uri == null) {
            return endPointHitRepository.getStatsUniq(startTime, endTime).stream().map(HitMapper::toViewStatsResponse)
                    .collect(Collectors.toList());
        } else if (!unique && uri != null) {
            return endPointHitRepository.getStatsByUri(uri, startTime, endTime).stream().map(HitMapper::toViewStatsResponse)
                    .collect(Collectors.toList());
        } else if (!unique && uri == null) {
            return endPointHitRepository.getStats(startTime, endTime).stream().map(HitMapper::toViewStatsResponse)
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("По данному запросу невозможно получить выборку");
    }

    public HitResponse saveHit(HitRequest hit) {
        final HitResponse hitResponse = HitMapper.toHitResponse(endPointHitRepository.save(HitMapper.toEndpointHit(hit)));
        log.info("В базу данных endpoint_hit сохранен: \n{}", hitResponse);
        return hitResponse;
    }
}

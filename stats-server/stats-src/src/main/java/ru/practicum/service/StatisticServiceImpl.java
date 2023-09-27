package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.constants.Constants;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.HitResponse;
import ru.practicum.dto.ViewStatsResponse;
import ru.practicum.exception.DateTimeFormatException;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {

    private final EndpointHitRepository endPointHitRepository;

    @Autowired
    public StatisticServiceImpl(EndpointHitRepository endPointHitRepository) {
        this.endPointHitRepository = endPointHitRepository;
    }

    @Override
    public List<ViewStatsResponse> getStats(String start, String end, List<String> uri, Boolean unique) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));
            LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));
            if (unique && uri != null) {
                if (uri.isEmpty()) {
                    return endPointHitRepository.getStatsUniq(startTime, endTime).stream().map(HitMapper::toViewStatsResponse)
                            .collect(Collectors.toList());
                } else {
                    return endPointHitRepository.getStatsByUriUniq(uri, startTime, endTime).stream().map(HitMapper::toViewStatsResponse)
                            .collect(Collectors.toList());
                }
            } else if (unique && uri == null) {
                return endPointHitRepository.getStatsUniq(startTime, endTime).stream().map(HitMapper::toViewStatsResponse)
                        .collect(Collectors.toList());
            } else if (!unique && uri != null) {
                if (uri.isEmpty()) {
                    return endPointHitRepository.getStats(startTime, endTime).stream().map(HitMapper::toViewStatsResponse)
                            .collect(Collectors.toList());
                } else {
                    return endPointHitRepository.getStatsByUri(uri, startTime, endTime).stream().map(HitMapper::toViewStatsResponse)
                            .collect(Collectors.toList());
                }
            } else if (!unique && uri == null) {
                return endPointHitRepository.getStats(startTime, endTime).stream().map(HitMapper::toViewStatsResponse)
                        .collect(Collectors.toList());
            }
            throw new IllegalArgumentException("По данному запросу невозможно получить выборку");
        } catch (DateTimeParseException e) {
            throw new DateTimeFormatException(String.format("Неверный формат даты - start: %s, end: %s", start, end));
        }
    }

    @Override
    public HitResponse saveHit(HitRequest hit) {
        try {
            final HitResponse hitResponse = HitMapper.toHitResponse(endPointHitRepository.save(HitMapper.toEndpointHit(hit)));
            log.debug("В базу данных endpoint_hit сохранен: \n{}", hitResponse);
            return hitResponse;
        } catch (DateTimeParseException e) {
            throw new DateTimeFormatException(String.format("Неверный формат даты: %s", hit.getTimestamp()));
        }
    }
}

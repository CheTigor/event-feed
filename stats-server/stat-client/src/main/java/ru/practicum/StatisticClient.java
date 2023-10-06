package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.ViewStatsResponse;

import java.util.List;

import static ru.practicum.constants.Constants.STAT_URL;

@Service
public class StatisticClient extends BaseClient {


    @Autowired
    public StatisticClient(@Value(STAT_URL) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveHit(HitRequest hit) {
        return post("/hit", hit);
    }

    public ResponseEntity<List<ViewStatsResponse>> getStats(String start, String end, String uris, Boolean uniq) {
        return getStats("/stats" + "?start=" + start + "&end=" + end + "&uris=" + uris + "&unique=" + uniq);
    }
}

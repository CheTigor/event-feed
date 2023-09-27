package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitRequest;

import java.util.List;

@Service
public class StatisticClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public StatisticClient(@Value("${statistic-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveHit(HitRequest hit) {
        return post("/hit", hit);
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean uniq) {
        return get("/stats" + "?start=" + start + "&end=" + end + "&uris=" + uris + "&unique=" + uniq);
    }

}

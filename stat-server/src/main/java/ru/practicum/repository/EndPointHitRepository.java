package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndPointHitRepository extends JpaRepository<EndpointHit, Long> {

    /*@Query(value = "SELECT eh.uri, eh.app, COUNT(eh.ip) " +
            "FROM endpoint_hit AS eh "+
            "WHERE eh.created BETWEEN ?1 AND ?2 "+
            "GROUP BY eh.uri, eh.app", nativeQuery = true)
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT eh.uri, eh.app, COUNT(DISTINCT eh.ip) " +
            "FROM endpoint_hit AS eh "+
            "WHERE eh.created BETWEEN ?1 AND ?2 "+
            "GROUP BY eh.uri, eh.app", nativeQuery = true)
    List<ViewStats> getStatsUniq(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT eh.uri, eh.app, COUNT(eh.ip) " +
            "FROM endpoint_hit AS eh "+
            "WHERE eh.uri IN ?1 AND eh.created BETWEEN ?2 AND ?3 "+
            "GROUP BY eh.uri, eh.app", nativeQuery = true)
    List<ViewStats> getStatsByUri(List<String> uri, LocalDateTime start, LocalDateTime end);*/
    @Query("SELECT new ru.practicum.model.ViewStats(eh.uri, eh.app, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit eh "+
            "WHERE eh.uri IN ?1 AND eh.created BETWEEN ?2 AND ?3 "+
            "GROUP BY eh.uri, eh.app " +
            "ORDER BY COUNT(DISTINCT eh.ip) desc")
    List<ViewStats> getStatsByUriUniq(List<String> uri, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.model.ViewStats(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit eh "+
            "WHERE eh.created BETWEEN ?1 AND ?2 "+
            "GROUP BY eh.uri, eh.app " +
            "ORDER BY COUNT(eh.ip) desc")
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit eh "+
            "WHERE eh.created BETWEEN ?1 AND ?2 "+
            "GROUP BY eh.uri, eh.app " +
            "ORDER BY COUNT(DISTINCT eh.ip) desc")
    List<ViewStats> getStatsUniq(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit eh "+
            "WHERE eh.uri in ?1 AND eh.created BETWEEN ?2 AND ?3 "+
            "GROUP BY eh.uri, eh.app " +
            "ORDER BY COUNT(eh.ip) desc")
    List<ViewStats> getStatsByUri(List<String> uri, LocalDateTime start, LocalDateTime end);
}

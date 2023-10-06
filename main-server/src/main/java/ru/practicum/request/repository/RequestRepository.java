package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequester_Id(Long userId);

    Request findByRequester_IdAndEvent_Id(Long userId, Long eventId);

    Optional<Request> findByIdAndRequester_Id(Long requestId, Long userId);

    List<Request> findByEvent_Id(Long eventId);

    List<Request> findByEvent_IdAndStatus(Long eventId, RequestStatus status);

    Optional<Request> findByIdAndEvent_Id(Long requestId, Long eventId);
}

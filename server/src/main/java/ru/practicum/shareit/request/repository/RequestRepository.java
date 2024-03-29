package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Collection<Request> findByRequestorId(Long userId, Sort sort);

    Collection<Request> findByRequestor_IdNotOrderByCreatedDesc(Long userId, PageRequest pageRequest);
}
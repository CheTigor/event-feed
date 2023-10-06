package ru.practicum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.user.model.User;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
}

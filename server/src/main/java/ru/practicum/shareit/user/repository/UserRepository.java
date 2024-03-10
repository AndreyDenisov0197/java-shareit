package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    default void checkUserById(Long userId) {
        if (!existsById(userId)) {
            throw new NotFoundException("There's no user with id " + userId);
        }
    }
}

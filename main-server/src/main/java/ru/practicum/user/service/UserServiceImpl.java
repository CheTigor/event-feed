package ru.practicum.user.service;

import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserResponseDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.QUser;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDto> getUsers(List<Long> userIds, Integer from, Integer size) {
        final QUser USER = QUser.user;
        BooleanBuilder builder = new BooleanBuilder(USER.isNotNull());
        if (userIds != null) {
            if (!userIds.isEmpty()) {
                builder.and(USER.id.in(userIds));
            }
        }
        Iterable<User> foundUsers = userRepository.findAll(builder, PageRequest.of(from / size, size));
        return UserMapper.toUserResponseDtoList(foundUsers);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        final UserResponseDto user = UserMapper.toResponseDto(userRepository.save(UserMapper.toUser(userRequestDto)));
        log.debug("В БД сохранен user: {}", user);
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.debug("Из БД удален user с id: {}", userId);
    }
}

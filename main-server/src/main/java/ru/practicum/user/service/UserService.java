package ru.practicum.user.service;

import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getUsers(List<Long> userIds, Integer from, Integer size);

    UserResponseDto createUser(UserRequestDto userRequestDto);

    void deleteUser(Long userId);


}

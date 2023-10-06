package ru.practicum.user.mapper;

import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserResponseDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getEmail(), user.getName());
    }

    public static User toUser(UserRequestDto requestDto) {
        return new User(null, requestDto.getEmail(), requestDto.getName());
    }

    public static List<UserResponseDto> toUserResponseDtoList(Iterable<User> iterable) {
        List<UserResponseDto> users = new ArrayList<>();
        iterable.forEach(user -> users.add(toResponseDto(user)));
        return users;
    }

    public static UserShortDto toUserShort(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}

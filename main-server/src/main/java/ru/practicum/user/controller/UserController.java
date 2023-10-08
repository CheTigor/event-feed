package ru.practicum.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserResponseDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@Valid
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDto> getUsers(@RequestParam(value = "ids", required = false) @NotNull List<Long> userIds,
                                          @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                          @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос getUsers - userId: \n{},\n from: \n{}, size: \n{}", userIds, from, size);
        final List<UserResponseDto> response = userService.getUsers(userIds, from, size);
        log.info("GET ответ getUsers - response: \n{}", response);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto user) {
        log.info("POST запрос postUser - userRequest: \n{}", user);
        final UserResponseDto response = userService.createUser(user);
        log.info("POST ответ getUser - response: \n{}", response);
        return response;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "userId") @Min(1) Long userId) {
        log.info("DELETE запрос deleteUser - userId: \n{}", userId);
        userService.deleteUser(userId);
        log.info("DELETE запрос для userId: {} выполнен успешно", userId);
    }
}

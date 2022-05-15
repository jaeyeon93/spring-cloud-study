package me.jimmy.userservice.service;

import me.jimmy.userservice.dto.UserDto;
import me.jimmy.userservice.entity.UserEntity;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();
}

package me.jimmy.userservice.repository;

import me.jimmy.userservice.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String username);
    UserEntity findByUserId(String userId);
}

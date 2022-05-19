package me.jimmy.userservice.controller;

import me.jimmy.userservice.controller.request.RequestUser;
import me.jimmy.userservice.dto.ResponseUser;
import me.jimmy.userservice.dto.UserDto;
import me.jimmy.userservice.entity.UserEntity;
import me.jimmy.userservice.service.UserService;
import me.jimmy.userservice.vo.Greeting;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("")
public class UsersController {
    private Environment env;
    private UserService userService;

    @Autowired
    private Greeting greeting;

    @Autowired
    public UsersController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's working in user service on PORT : %S", env.getProperty("local.server.port"));
    }

    @GetMapping("/welcome")
    public String welcome() {
        return env.getProperty("greeting.message");
    }

    @GetMapping("/welcome2")
    public String welcome2() {
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(user -> result.add(new ModelMapper().map(user, ResponseUser.class)));

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);
        ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}

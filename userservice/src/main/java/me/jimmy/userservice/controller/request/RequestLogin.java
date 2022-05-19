package me.jimmy.userservice.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestLogin {

    @NotNull
    private String email;

    @NotNull
    private String password;
}

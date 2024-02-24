package com.tune_fun.v1.account.application.port.input.query;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AccountQueries {

    public record Username(
            @NotBlank(message = "{email.not_blank}")
            @Email(message = "{email.format}") String email) {}

    public record Info() {

    }

}

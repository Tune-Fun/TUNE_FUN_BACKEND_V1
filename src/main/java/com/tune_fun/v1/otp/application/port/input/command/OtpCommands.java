package com.tune_fun.v1.otp.application.port.input.command;

public class OtpCommands {

    public record Resend(String username, String otpType) {}

}

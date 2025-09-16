package com.techmanager.techmanager.user.dto;

import com.techmanager.techmanager.user.UserType;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UserRequest(
    @NotBlank @Size(max = 200) String fullName,
    @NotBlank @Email @Size(max = 255) String email,
    @NotBlank @Pattern(regexp = "^\\+\\d{1,3}\\s\\d{1,3}\\s\\d{4,5}-\\d{4}$", message = "Use international format, ex: +55 11 99999-9999") String phone,
    @NotNull @Past LocalDate birthDate,
    @NotNull UserType userType) {
}
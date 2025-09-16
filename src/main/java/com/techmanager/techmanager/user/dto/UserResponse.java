package com.techmanager.techmanager.user.dto;

import com.techmanager.techmanager.user.UserType;
import java.time.LocalDate;

public record UserResponse(
    Long id, String fullName, String email, String phone, LocalDate birthDate, UserType userType) {
}

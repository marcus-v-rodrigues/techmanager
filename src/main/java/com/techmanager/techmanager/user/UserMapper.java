package com.techmanager.techmanager.user;

import com.techmanager.techmanager.user.dto.*;

public class UserMapper {

  private UserMapper() {
    // private constructor to avoid instantiation
  }

  public static User toEntity(UserRequest r) {
    return User.builder()
        .fullName(r.fullName()).email(r.email()).phone(r.phone())
        .birthDate(r.birthDate()).userType(r.userType()).build();
  }

  public static UserResponse toResponse(User u) {
    return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getPhone(), u.getBirthDate(), u.getUserType());
  }

  public static void update(User u, UserRequest r) {
    u.setFullName(r.fullName());
    u.setEmail(r.email());
    u.setPhone(r.phone());
    u.setBirthDate(r.birthDate());
    u.setUserType(r.userType());
  }
}
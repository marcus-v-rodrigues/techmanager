package com.techmanager.techmanager.user;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String msg) {
    super(msg);
  }
}

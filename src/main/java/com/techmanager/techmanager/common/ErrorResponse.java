package com.techmanager.techmanager.common;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    String code,
    String message,
    Instant timestamp,
    Map<String, String> details) {
  public static ErrorResponse of(String code, String message) {
    return new ErrorResponse(code, message, Instant.now(), null);
  }

  public static ErrorResponse of(String code, String message, Map<String, String> details) {
    return new ErrorResponse(code, message, Instant.now(), details);
  }
}

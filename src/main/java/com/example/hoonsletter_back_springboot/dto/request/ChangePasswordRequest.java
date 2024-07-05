package com.example.hoonsletter_back_springboot.dto.request;

public record ChangePasswordRequest(
    String currentPassword,
    String newPassword,
    String confirmPassword
) {
  public static ChangePasswordRequest of(String currentPassword,
      String newPassword,
      String confirmPassword) {
    return new ChangePasswordRequest(currentPassword,
        newPassword,
        confirmPassword);
  }

}

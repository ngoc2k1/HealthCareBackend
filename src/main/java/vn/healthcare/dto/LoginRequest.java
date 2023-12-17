package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {
    @NotBlank(message = "Email hoặc số điện thoại không được để trống")
    private String phoneOrEmail;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}

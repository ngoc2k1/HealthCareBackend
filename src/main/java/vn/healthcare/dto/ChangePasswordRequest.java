package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;
}

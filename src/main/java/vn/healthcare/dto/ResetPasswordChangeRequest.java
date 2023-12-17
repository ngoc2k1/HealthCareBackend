package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordChangeRequest {
    @NotBlank(message = "OTP không được để trống")
    private String resetPasswordCode;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String newPassword;
}

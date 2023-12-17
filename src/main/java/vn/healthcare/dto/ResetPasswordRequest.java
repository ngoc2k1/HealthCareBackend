package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Email không được để trống")
    private String email;
}

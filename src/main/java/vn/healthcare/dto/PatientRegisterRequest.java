package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.healthcare.constant.Gender;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PatientRegisterRequest {
    private String phone;

    private String password;

    private String name;

    private String birthday;

    private Gender gender;

    private String address;

    private String email;
}

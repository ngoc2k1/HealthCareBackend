package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.healthcare.constant.Gender;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDoctorProfileRequest {
    private String phone;

    private String name;

    private String birthday;

    private String addressTest;

    private String avatar;

    private Gender gender;

    private String identityCard;

    private String healthInsurance;

}

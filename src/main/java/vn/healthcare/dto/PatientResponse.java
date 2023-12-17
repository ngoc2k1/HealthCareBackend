package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.healthcare.constant.BloodGroup;
import vn.healthcare.constant.Gender;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientResponse {
    private Integer id;

    private Integer height;

    private Double weight;

    private BloodGroup bloodGroup;

    private String phone;

    private String name;

    private String birthday;

    private Integer age;

    private String email;

    private String address;

    private String avatar;

    private Gender gender;

    private String identityCard;

    private String healthInsurance;
}

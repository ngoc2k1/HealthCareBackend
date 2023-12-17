package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.healthcare.constant.Gender;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDoctorRequest {
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;


    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Ngày sinh không được để trống")
    private String birthday;


    @NotBlank(message = "Địa chỉ không được để trống")
    private String addressTest;

    @NotBlank(message = "Email không được để trống")
    private String email;


    @NotBlank(message = "Ảnh không được để trống")
    private String avatar;

    private Gender gender = Gender.MALE;

    @NotBlank(message = "Mã định danh không được để trống")
    private String identityCard;


    @NotBlank(message = "Bảo hiểm không được để trống")
    private String healthInsurance;


    @NotNull(message = "Chuyên môn không được để trống")
    private Integer specialtyId;

    private String password;
}

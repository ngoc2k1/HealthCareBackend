package vn.healthcare.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.healthcare.constant.Gender;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DoctorResponse {
    private Integer id;

    private SpecialtyResponse specialty;

    private String phone;

    private String email;

    private String name;

    private String birthday;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer age;

    private String addressTest;

    private String avatar;

    private Gender gender;

    private String identityCard;

    private String healthInsurance;

    private LocalDateTime createdAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class SpecialtyResponse {
        private Integer id;

        private String name;

        private String image;
    }
}

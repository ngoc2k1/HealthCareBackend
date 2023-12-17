package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.healthcare.constant.Gender;
import vn.healthcare.constant.StatusBook;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookScheduleResponse {
    private Integer id;

    private PatientResponse patient;

    private String dateTest;

    private String timeTest;

    private StatusBook statusBook;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PatientResponse {
        private Integer id;
        private String phone;
        private String name;
        private Integer age;
        private String avatar;
        private Gender gender;
    }
}

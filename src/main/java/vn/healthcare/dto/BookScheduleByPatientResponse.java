
package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.healthcare.constant.Gender;
import vn.healthcare.constant.StatusBook;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookScheduleByPatientResponse {
    private Integer id;

    private PatientResponse patient;

    private DoctorResponse doctor;

    private StatusBook statusBook;

    private String namePatientTest;

    private String statusHealth;

    private String qrCode;

    private Integer price;

    private String dateTest;

    private String timeTest;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PatientResponse {
        private Integer id;
        private String name;
        private Integer age;
        private String avatar;
        private Gender gender;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class DoctorResponse {
        private Integer id;
        private String name;
        private String avatar;
        private String addressTest;


        private SpecialtyResponse specialty;


        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class SpecialtyResponse {
            private String name;
            private String image;

        }
    }
}

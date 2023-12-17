package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.healthcare.constant.Gender;
import vn.healthcare.constant.StatusBook;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DoctorNotificationResponse {
    private BookScheduleResponse bookSchedule;
    private PatientResponse patient;
    private String content;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class BookScheduleResponse {
        private Integer id;
        private String datTest;
        private String timeTest;
        private StatusBook statusBook;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class PatientResponse {
        private Integer id;
        private String name;
        private Integer age;
        private Gender gender;
    }
}

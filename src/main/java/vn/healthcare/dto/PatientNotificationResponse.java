package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PatientNotificationResponse {
    private BookScheduleResponse bookSchedule;
    private DoctorResponse doctor;
    private String content;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class BookScheduleResponse {
        private Integer id;
        private String datTest;
        private String timeTest;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class DoctorResponse {
        private Integer id;
        private String name;
        private Specialty specialty;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class Specialty {
            private String name;
        }
    }
}

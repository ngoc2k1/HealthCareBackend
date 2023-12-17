package vn.healthcare.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicalHistoryResponse {
    private Integer id;
    private BookScheduleData bookSchedule;
    private String retestDate;
    private String judgmentNote;
    private String testResult;
    private String prescription;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class BookScheduleData {
        private Integer id;
        private DoctorData doctor;
        private String namePatientTest;
        private String dateTest;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class DoctorData {
            private Integer id;
            private String name;
            private String avatar;
            private SpecialtyData specialty;
            private String addressTest;

            @AllArgsConstructor
            @NoArgsConstructor
            @Data
            public static class SpecialtyData {
                private String name;
                private String image;
            }
        }
    }
}

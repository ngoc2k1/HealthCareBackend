package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateScheduleRequest {
    private Integer doctorWorkScheduleId;
    private String namePatientTest;
    private String statusHealth;
    private String qrcode;
}

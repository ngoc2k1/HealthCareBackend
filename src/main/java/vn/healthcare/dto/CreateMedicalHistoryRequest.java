package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMedicalHistoryRequest {
    private Integer bookScheduleId;
    private String retestDate;
    private String judgmentNote;
    private String testResult;
    private String prescription;
}

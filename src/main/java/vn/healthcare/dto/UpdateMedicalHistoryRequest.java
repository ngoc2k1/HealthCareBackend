package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateMedicalHistoryRequest {
    private String retestDate;
    private String judgmentNote;
    private String testResult;
    private String prescription;
}

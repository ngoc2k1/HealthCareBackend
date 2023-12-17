package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TimeByDoctorResponse {
    private Integer id;
    private String value;
    private Integer price;
    private Boolean isBooked;
}

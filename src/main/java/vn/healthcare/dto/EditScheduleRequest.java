package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EditScheduleRequest {
    @NotNull(message = "Bác sĩ không được để trống")
    private Integer doctorId;


    @NotBlank(message = "Ngày không được để trống")
    private String date;

    private List<Time> times;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Time {
        private Integer id;
        private String start;
        private String end;
        private Integer price;
    }
}

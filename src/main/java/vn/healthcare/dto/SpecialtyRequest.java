package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpecialtyRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Hình ảnh không được để trống")
    private String image;
}

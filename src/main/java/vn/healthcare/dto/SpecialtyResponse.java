package vn.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpecialtyResponse {
    private String name;

    private String image;

    private Integer id;

    private LocalDateTime createdAt;
}

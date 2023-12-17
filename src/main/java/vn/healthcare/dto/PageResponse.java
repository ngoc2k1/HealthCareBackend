package vn.healthcare.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse {
    private Integer code;

    private String msg;

    private Object data;

    private Integer currentPage;

    private Integer totalPage;

    private Integer perPage;
}

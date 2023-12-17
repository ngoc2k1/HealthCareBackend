package vn.healthcare.exception;

import lombok.Data;

@Data
public class BadRequestException  extends RuntimeException {
    private String data;

    public BadRequestException(String data, String message) {
        super(message);
        this.data = data;
    }


    public BadRequestException(String message) {
        super(message);
    }
}

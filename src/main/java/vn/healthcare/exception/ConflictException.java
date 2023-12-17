package vn.healthcare.exception;

import lombok.Data;

@Data
public class ConflictException extends RuntimeException {
    private String data;

    public ConflictException(String data, String message) {
        super(message);
        this.data = data;
    }
}

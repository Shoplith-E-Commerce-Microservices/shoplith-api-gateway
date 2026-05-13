package com.shoplith.api_gateway.response;



import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;
    private Status status;

    public enum Status {
        ERROR,
        SUCCESS,
        PENDING
    }

    public ApiResponse(int code, String message, Status status, T data) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public ApiResponse(int code, String message, Status status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
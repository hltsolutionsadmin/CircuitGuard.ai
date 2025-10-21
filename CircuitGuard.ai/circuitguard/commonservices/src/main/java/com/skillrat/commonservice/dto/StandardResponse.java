package com.skillrat.commonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardResponse<T> {
    private String message;
    private String status;
    private T data;

    public static <T> StandardResponse<T> single(String message, T data) {
        return new StandardResponse<>(message, "success", data);
    }

    public static <T> StandardResponse<List<T>> list(String message, List<T> dataList) {
        return new StandardResponse<>(message, "success", dataList);
    }

    public static <T> StandardResponse<Iterable<T>> list(String message, Iterable<T> dataList) {
        return new StandardResponse<>(message, "success", dataList);
    }

    public static <T> StandardResponse<Page<T>> page(String message, Page<T> data) {
        return new StandardResponse<>(message, "success", data);
    }

    public static <T> StandardResponse<T> message(String message) {
        return new StandardResponse<>(message, "success", null);
    }

    public static <T> StandardResponse<T> error(String message) {
        return new StandardResponse<>(message, "error", null);
    }
}

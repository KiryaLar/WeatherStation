package ru.larkin.producer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ErrorResponse {
    private Date timestamp;
    private String message;
}

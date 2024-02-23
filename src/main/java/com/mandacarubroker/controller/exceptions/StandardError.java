package com.mandacarubroker.controller.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
@Setter
public class StandardError {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String error;

}

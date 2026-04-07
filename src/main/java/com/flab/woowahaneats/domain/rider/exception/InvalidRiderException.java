package com.flab.woowahaneats.domain.rider.exception;

import org.springframework.http.HttpStatus;

public class InvalidRiderException extends RiderException {
    public InvalidRiderException(String message) {
        super(
                message,
                "INVALID_RIDER",
                HttpStatus.BAD_REQUEST
        );
    }
}

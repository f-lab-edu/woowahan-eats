package com.flab.woowahaneats.domain.rider.exception;

import org.springframework.http.HttpStatus;

public class InvalidRiderStatusException extends RiderException {
    public InvalidRiderStatusException(String message) {
        super(
                message,
                "INVALID_RIDER_STATUS",
                HttpStatus.BAD_REQUEST
        );
    }
}
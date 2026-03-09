package com.flab.woowahaneats.domain.auth.exception;

public class InvalidRoleException extends RuntimeException {

    public InvalidRoleException(String requiredRole) {
        super(requiredRole + " 권한이 필요합니다");
    }
}
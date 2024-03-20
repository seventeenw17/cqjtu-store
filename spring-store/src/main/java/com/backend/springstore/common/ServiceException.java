package com.backend.springstore.common;

import lombok.Getter;

public class ServiceException extends RuntimeException {
    @Getter
    private ServiceCode serviceCode;

    public ServiceException(ServiceCode serviceCode, String message) {
        super(message);
        this.serviceCode = serviceCode;
    }
}

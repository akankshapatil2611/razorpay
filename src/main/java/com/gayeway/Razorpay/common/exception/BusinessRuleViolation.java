package com.gayeway.Razorpay.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessRuleViolation extends RuntimeException{

    private final String errorCode;

    public BusinessRuleViolation(String errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode;
    }

}

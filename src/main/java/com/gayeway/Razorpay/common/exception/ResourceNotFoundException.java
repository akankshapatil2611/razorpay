package com.gayeway.Razorpay.common.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{

    private final String resourceName;
    private final Object identifier;

    public ResourceNotFoundException(String resourceName, Object identifier)
    {
        super();
        this.resourceName = resourceName;
        this.identifier = identifier;
    }
}

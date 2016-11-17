package com.brianscottrussell.web.exceptions;

/*
 * Copyright (c) 2016 Brian Scott Russell. All Rights Reserved.
 * The source code is owned by Brian Scott Russell and is protected by copyright
 * laws and international copyright treaties, as well as other intellectual
 * property laws and treaties.
 */

/**
 * @author brussell
 */
public class RecaptchaServiceException extends RuntimeException {

    public RecaptchaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
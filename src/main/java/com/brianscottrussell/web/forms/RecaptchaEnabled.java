package com.brianscottrussell.web.forms;

/*
 * Copyright (c) 2016 Brian Scott Russell. All Rights Reserved.
 * The source code is owned by Brian Scott Russell and is protected by copyright
 * laws and international copyright treaties, as well as other intellectual
 * property laws and treaties.
 */

/**
 * RecaptchaEnabled
 *
 * This interface requires the implementer to implement a get/set for recaptchaResponse String
 *
 * @author brussell
 */
public interface RecaptchaEnabled {

    String getRecaptchaResponse();

    void setRecaptchaResponse(String recaptchaResponse);

}

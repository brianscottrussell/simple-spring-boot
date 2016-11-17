package com.brianscottrussell.web.forms;

/*
 * Copyright (c) 2016 Brian Scott Russell. All Rights Reserved.
 * The source code is owned by Brian Scott Russell and is protected by copyright
 * laws and international copyright treaties, as well as other intellectual
 * property laws and treaties.
 */

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author brussell
 */
public class ContactForm implements RecaptchaEnabled {

    private String name;
    private String email;
    private String phone;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Recaptcha property/methods
   	@NotEmpty( message = "{recaptcha.error.empty}" )
   	private String recaptchaResponse;

   	@Override
   	public String getRecaptchaResponse() {
   		return recaptchaResponse;
   	}

   	@Override
   	public void setRecaptchaResponse(String recaptchaResponse) {
   		this.recaptchaResponse = recaptchaResponse;
   	}

    @Override
    public String toString() {
        return "ContactForm{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", message='" + message + '\'' +
                ", recaptchaResponse='" + recaptchaResponse + '\'' +
                '}';
    }
}

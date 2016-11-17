package com.brianscottrussell.service;

/*
 * Copyright (c) 2016 Brian Scott Russell. All Rights Reserved.
 * The source code is owned by Brian Scott Russell and is protected by copyright
 * laws and international copyright treaties, as well as other intellectual
 * property laws and treaties.
 */

import org.springframework.mail.MailException;

import javax.mail.MessagingException;

import java.io.UnsupportedEncodingException;

/**
 * @author brussell
 */
public interface EmailService {

    /**
     * Provide a subject & HTML body, along with from & to information and it'll be sent
     *
     * @param subject String
     * @param htmlBody String Assumed to be HTML
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    void sendSimpleEmail(String subject, String htmlBody, String fromEmail, String fromName, String toEmail) throws MessagingException, UnsupportedEncodingException, MailException;
}

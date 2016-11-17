package com.brianscottrussell.service;

/*
 * Copyright (c) 2016 Brian Scott Russell. All Rights Reserved.
 * The source code is owned by Brian Scott Russell and is protected by copyright
 * laws and international copyright treaties, as well as other intellectual
 * property laws and treaties.
 */

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * @author brussell
 */
@Primary
@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = Logger.getLogger(EmailServiceImpl.class);

    /**
   	 * This is configured in Spring Boot Auto-Configure
   	 * spring-boot-starter-mail https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-email.html#boot-features-email
   	 */
   	@SuppressWarnings("SpringJavaAutowiringInspection")
   	@Autowired
   	private JavaMailSender mailSender;

    /**
     * Nothing fancy. Just provide a subject & HTML body to the email and it'll be sent
     *
     * @param subject String
     * @param htmlBody String Assumed to be HTML
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @Override
    public void sendSimpleEmail(String subject, String htmlBody, String fromEmail, String fromName, String toEmail) throws MessagingException, UnsupportedEncodingException, MailException {
        logger.debug("Sending contact email with subject: " + subject );

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

        message.setSubject(subject);
        message.setFrom(fromEmail, fromName);
        message.setTo(toEmail);
        message.setText(htmlBody, true /* isHtml */);

        // Send email
        this.mailSender.send(mimeMessage);
    }
}

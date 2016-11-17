package com.brianscottrussell.service;

/*
 * Copyright (c) 2016 Brian Scott Russell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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

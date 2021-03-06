package com.brianscottrussell.web.app;

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

import com.brianscottrussell.service.EmailService;
import com.brianscottrussell.web.forms.ContactForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author brussell
 */
@Controller
public class HomeController {
    private static final Logger logger = Logger.getLogger(HomeController.class);

    @Autowired
    EmailService emailService;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "home";
    }

    /**
     * Makes the recaptchaSiteKey available to the view
     *
     * @param recaptchaSiteKey String
     * @return String
     */
    @ModelAttribute("recaptchaSiteKey")
    public String getRecaptchaSiteKey(@Value("${recaptcha.site-key}") String recaptchaSiteKey) {
        return recaptchaSiteKey;
    }

    @Value("${site.url}")
    private String siteUrl;
    @Value("${site.name}")
    private String siteName;
    @Value("${contact.form.from.email}")
    private String fromEmail;
    @Value("${contact.form.from.name}")
    private String fromName;
    @Value("${contact.form.to.email}")
    private String toEmail;

    @RequestMapping(value = "/contact/submit", method = RequestMethod.POST)
    public String contactSubmit(@Valid @ModelAttribute("contactForm") ContactForm form
                                , BindingResult formBindingResult
                                , RedirectAttributes redirectAttributes
    ) {
        logger.debug(form);

        if (formBindingResult.hasErrors()) {
            logger.debug("Validation Errors Present: " + formBindingResult);
            return "home";
        }

        Assert.notNull(form);

        StringBuilder htmlBody = new StringBuilder()
                .append("<p>You've received a new message sent via the Contact Me Form on <a href=\"").append( siteUrl ).append("\">").append( siteName ).append("</a></p>")
                .append("<p><span style=\"font-weight:bold;\">Date: </span><span>").append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())).append("</span></p>")
                .append("<p><span style=\"font-weight:bold;\">Name: </span><span>").append(form.getName()).append("</span></p>")
                .append("<p><span style=\"font-weight:bold;\">Email Address: </span><span><a href=\"mailto:").append(form.getEmail()).append("\">").append(form.getEmail()).append("</a></span></p>")
                .append("<p><span style=\"font-weight:bold;\">Phone Number: </span><span>").append(form.getPhone()).append("</span></p>")
                .append("<p><span style=\"font-weight:bold;\">Message</span></p>")
                .append("<p>").append(form.getMessage()).append("</p>")
                .append("<p></p>")
                .append("<p></p>")
                .append("<p></p>")
                .append("<p>Sent from <a href=\"").append( siteUrl ).append("\">").append( siteName ).append("</a></p>")
                ;

        try {
            emailService.sendSimpleEmail("Message from " + siteName, htmlBody.toString(), fromEmail, fromName, toEmail);
            redirectAttributes.addFlashAttribute("successMessage", "Thank you for contacting me!");
        } catch (MailException|MessagingException|UnsupportedEncodingException e) {
            logger.error(e.getClass().getSimpleName() + ": " + e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "There was an issue sending your message.");
        }
        return "redirect:/";
    }

}

package com.brianscottrussell.web.validators;

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

import com.brianscottrussell.service.RecaptchaService;
import com.brianscottrussell.web.exceptions.RecaptchaServiceException;
import com.brianscottrussell.web.forms.RecaptchaEnabled;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @author brussell
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RecaptchaFormValidator implements Validator {

    private static final Logger logger = Logger.getLogger(RecaptchaFormValidator.class);

    private static final String ERROR_RECAPTCHA_INVALID = "recaptcha.error.invalid";
    private static final String ERROR_RECAPTCHA_UNAVAILABLE = "recaptcha.error.unavailable";
    private final HttpServletRequest httpServletRequest;
    private final RecaptchaService recaptchaService;

    @Autowired
    public RecaptchaFormValidator(HttpServletRequest httpServletRequest, RecaptchaService recaptchaService) {
        this.httpServletRequest = httpServletRequest;
        this.recaptchaService = recaptchaService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RecaptchaEnabled.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RecaptchaEnabled form = (RecaptchaEnabled) target;

        logger.debug("Validating: " + form);
        try {
            if (form.getRecaptchaResponse() != null
                    && !form.getRecaptchaResponse().isEmpty()
                    && !recaptchaService.isResponseValid(getRemoteIp(httpServletRequest), form.getRecaptchaResponse())) {
                errors.reject(ERROR_RECAPTCHA_INVALID);
                errors.rejectValue("recaptchaResponse", ERROR_RECAPTCHA_INVALID);
            }
        } catch (RecaptchaServiceException e) {
            errors.reject(ERROR_RECAPTCHA_UNAVAILABLE);
        }
        logger.debug("Errors resulting from validation: " + errors);
    }

    /**
     * Helper method used to get the forwarded IP since we run Tomcat behind a proxied apache request
     *
     * From http://kielczewski.eu/2015/07/spring-recaptcha-v2-form-validation/:
     * By the way, the IP address returned by httpServletRequest.getRemoteAddr() can bo not a real user's IP address.
     * It all depends on your environment. I very often use a proxy that proxies request from http://somedomain/ to an actual Java application running on http://somelocalmachine:8080/.
     * Since in that case the actual requests made to a Java application come from the proxy, therefore getRemoteAddr() call will return proxy's IP address instead of the user's.
     * Fortunately, most proxies can be configured to pass the real IP address in request headers.
     *
     * @param request HttpServletRequest
     * @return String
     */
    private String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

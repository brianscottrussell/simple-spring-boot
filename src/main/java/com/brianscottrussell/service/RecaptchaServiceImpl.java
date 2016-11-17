package com.brianscottrussell.service;

/*
 * Copyright (c) 2016 Brian Scott Russell. All Rights Reserved.
 * The source code is owned by Brian Scott Russell and is protected by copyright
 * laws and international copyright treaties, as well as other intellectual
 * property laws and treaties.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.brianscottrussell.web.exceptions.RecaptchaServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

/**
 * @author brussell
 */
@Service
public class RecaptchaServiceImpl implements RecaptchaService {

    private static final Logger logger = Logger.getLogger(RecaptchaServiceImpl.class);

    private static class RecaptchaResponse {
        @JsonProperty("success")
        private boolean success;
        @JsonProperty("error-codes")
        private Collection<String> errorCodes;
    }

    private final RestTemplate restTemplate;

    @Value("${recaptcha.url}")
    private String recaptchaUrl;

    @Value("${recaptcha.secret-key}")
    private String recaptchaSecretKey;

    @Autowired
    public RecaptchaServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean isResponseValid(String remoteIp, String response) {
        logger.debug("Call to isResponseValid(" + remoteIp + ", " + response + ')');
        RecaptchaResponse recaptchaResponse;
        try {
            recaptchaResponse = restTemplate.postForEntity(
                    recaptchaUrl, createBody(recaptchaSecretKey, remoteIp, response), RecaptchaResponse.class)
                    .getBody();
            logger.debug("RecaptchaResponse: " + recaptchaResponse);
        } catch (RestClientException e) {
            throw new RecaptchaServiceException("Recaptcha API not available due to exception", e);
        }
        return recaptchaResponse.success;
    }

    private MultiValueMap<String, String> createBody(String secret, String remoteIp, String response) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", secret);
        form.add("remoteip", remoteIp);
        form.add("response", response);
        return form;
    }
}

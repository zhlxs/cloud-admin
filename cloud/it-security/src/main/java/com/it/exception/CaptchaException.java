package com.it.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @author HSL
 * @date 2021-11-15 22:39
 * @desc 验证码异常
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg) {
        super(msg);
    }
}

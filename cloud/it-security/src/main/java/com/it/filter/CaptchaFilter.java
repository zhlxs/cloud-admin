package com.it.filter;

import cn.hutool.core.util.StrUtil;
import com.it.exception.CaptchaException;
import com.it.security.LoginFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author HSL
 * @date 2021-11-15 22:25
 * @desc 验证码过滤器
 */
//@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String url = request.getRequestURI();
        if ("/login".equals(url) && request.getMethod().equals("POST")) {
            // 校验验证码
            try {
                validateCode(request);
            } catch (CaptchaException e) {
                // 如果不正确就跳转到失败处理器
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }
    }

    /*
     * @author HSL
     * @date 2021-11-15
     * @desc 验证码
     **/
    private void validateCode(HttpServletRequest request) {
        // 前端验证码
        String code = request.getParameter("code");
        String key = request.getParameter("x-key");
        if (StrUtil.isBlank(code) || StrUtil.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }
        // 从redis获取验证码进行校验
    }
}

package com.it.security;

import cn.hutool.json.JSONUtil;
import com.it.entity.ApiResult;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author HSL
 * @date 2021-11-15 22:08
 * @desc 登录失败处理器
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        ApiResult<Integer> result;
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (ex instanceof UsernameNotFoundException || ex instanceof BadCredentialsException) {
            result = ApiResult.success(HttpServletResponse.SC_UNAUTHORIZED, "用户名或密码错误");
        } else if (ex instanceof DisabledException) {
            result = ApiResult.success(HttpServletResponse.SC_UNAUTHORIZED, "账户被禁用");
        } else {
            result = ApiResult.success(HttpServletResponse.SC_UNAUTHORIZED, "登录失败");
        }
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}

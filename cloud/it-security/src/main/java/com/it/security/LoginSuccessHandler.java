package com.it.security;

import cn.hutool.json.JSONUtil;
import com.it.dto.LoginRespDTO;
import com.it.dto.LoginUser;
import com.it.dto.Token;
import com.it.entity.ApiResult;
import com.it.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author HSL
 * @date 2021-11-15 22:08
 * @desc 登录成功处理器
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        // 生成JWT,并且放置到请求头中
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Token token = tokenService.saveToken(loginUser);
        LoginRespDTO rs = new LoginRespDTO();
        rs.setJwtToken(token);
        rs.setUser(loginUser);
        ApiResult<LoginRespDTO> result = ApiResult.success(rs, "登录成功");
        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}

package com.it.security;

import cn.hutool.json.JSONUtil;
import com.it.entity.ApiResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author HSL
 * @date 2021-11-20 14:51
 * @desc 登出处理器
 */
@Component
public class OutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        ApiResult<Authentication> result = ApiResult.success(authentication, "退出成功");
        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}

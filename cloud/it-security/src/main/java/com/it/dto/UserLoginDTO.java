package com.it.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author HSL
 * @date 2021-11-14 16:59
 * @desc 登录请求参数
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserLoginDTO implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}

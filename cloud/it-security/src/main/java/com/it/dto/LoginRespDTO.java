package com.it.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author HSL
 * @date 2021-11-14 16:53
 * @desc 登录返参
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginRespDTO implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * token信息
     */
    private Token jwtToken;

    /**
     * 用户信息
     */
    private LoginUser user;
}

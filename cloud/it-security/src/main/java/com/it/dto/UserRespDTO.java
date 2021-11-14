package com.it.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author HSL
 * @date 2021-11-14 17:00
 * @desc 登录用户信息返参
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserRespDTO implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;
}

//package com.it.web;
//
//import com.it.dto.LoginRespDTO;
//import com.it.dto.UserLoginDTO;
//import com.it.entity.ApiResult;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//
///**
// * @author HSL
// * @date 2021-11-14 16:51
// * @desc 登录服务
// */
//@Api(tags = "用户服务")
//@RestController
//@RequestMapping("/login")
//@Slf4j
//@Validated
//public class LoginController {
//
//    @ApiOperation(value = "登录")
//    @PostMapping("/login")
//    public ApiResult<LoginRespDTO> login(@Valid @RequestBody UserLoginDTO loginParams) {
//        // 登录验证
//        // 查询用户的权限(角色)
//        // 这里需要颁发token
//        // 临时用户
//        return null;
//    }
//}

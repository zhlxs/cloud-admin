package com.it.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HSL
 * @date 2021-11-20 11:30
 * @desc 测试
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String testApi() {
        return "success";
    }
}

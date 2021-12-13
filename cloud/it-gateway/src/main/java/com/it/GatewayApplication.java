package com.it;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author HSL
 * @date 2021-11-14 14:48
 * @desc 启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        /**
         * 需设置Maven  > Runner  > Delegate IDE build/run actions to Maven。
         * 也就是将IDE构建/运行操作委托给Maven。
         */
        // 访问${GATEWAY_URL}/{微服务名称X}/**会转发到微服务X的/**路径下面
        SpringApplication.run(GatewayApplication.class);
    }
}
// token的过期时间存放在其里面，很难控制
// Session Store(JSESSIONID存储)
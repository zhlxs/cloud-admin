package com.it.service;

import cn.hutool.json.JSONArray;
import com.it.entity.GatewayDTO;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author HSL
 * @date 2021-11-19 0:06
 * @desc 路由服务
 */
public interface SysRouteConfService {

    /**
     * @author HSL
     * @date 2021-11-19
     * @desc 获取全部路由
     **/
    List<GatewayDTO> findAllRoutes();

    /**
     * @author HSL
     * @date 2021-11-19
     * @desc 更新路由信息
     **/
    Mono<Void> editRoute(JSONArray routes);

    /**
     * @author HSL
     * @date 2021-11-19
     * @desc 新增路由
     **/
    String createRoute(GatewayDTO dto);
}

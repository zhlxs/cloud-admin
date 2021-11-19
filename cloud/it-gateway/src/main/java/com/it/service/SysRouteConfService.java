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

    /**
     * @Author: HSL
     * @Date: 2021/11/19 11:24
     * @Desc: 刷新路由
     **/
    String refreshGatewayRouteIntoRouteDefinitionWriter();

    /**
     * @Author: HSL
     * @Date: 2021/11/19 13:48
     * @Desc: 删除路由
     **/
    void deleteRoute(String routeId);
}

package com.it.service.impl;

import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.it.dao.GatewayDao;
import com.it.entity.GatewayDTO;
import com.it.service.SysRouteConfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HSL
 * @date 2021-11-19 0:14
 * @desc
 */
@Service("sysRouteConfService")
@Slf4j
public class SysRouteConfServiceImpl implements SysRouteConfService, ApplicationEventPublisherAware, CommandLineRunner {

    @Autowired
    private GatewayDao gatewayDao;
    @Autowired
    private RedisRouteDefinitionRepository routeDefinitionWriter;

    @Override
    public List<GatewayDTO> findAllRoutes() {
        return new LambdaQueryChainWrapper<>(gatewayDao).eq(GatewayDTO::getDelFlag, 0).list();
    }

    @Override
    public Mono<Void> editRoute(JSONArray routes) {
        // 清空Redis 缓存
        return Mono.empty();
    }

    @Override
    public String createRoute(GatewayDTO dto) {
        gatewayDao.insert(dto);
        return "保存成功";
    }

    /**
     * @Author: HSL
     * @Date: 2021/11/19 13:47
     * @Desc: 刷新路由信息
     **/
    @Override
    public String refreshGatewayRouteIntoRouteDefinitionWriter() {
        List<GatewayDTO> gatewayList = findAllRoutes();
        log.info("网关配置信息：=====>" + JSON.toJSONString(gatewayList));
        gatewayList.forEach(route -> {
            RouteDefinition definition = new RouteDefinition();
            Map<String, String> predicateParams = new HashMap<>(8);
            PredicateDefinition predicate = new PredicateDefinition();
            FilterDefinition filterDefinition = new FilterDefinition();
            Map<String, String> filterParams = new HashMap<>(8);
            definition.setId(route.getId().toString());
            predicate.setName("Path");
            // predicateParams.put("pattern", "/api" + route.getUri());
            // predicateParams.put("pathPattern", "/api" + route.getPath());
            URI uri = UriComponentsBuilder.fromUriString("lb://" + route.getRouteId()).build().toUri();
            filterDefinition.setName("StripPrefix");
            // 路径去前缀
            // filterParams.put("_genkey_0", route.getStripPrefix().toString());
            // 令牌桶流速
            // filterParams.put("redis-rate-limiter.replenishRate", route.getLimiterRate());
            // 令牌桶容量
            // filterParams.put("redis-rate-limiter.burstCapacity", route.getLimiterCapacity());
            // 限流策略(#{@BeanName})
            // filterParams.put("key-resolver", "#{@remoteAddrKeyResolver}");
            // predicate.setArgs(predicateParams);
            // filterDefinition.setArgs(filterParams);
            // definition.setPredicates(Arrays.asList(predicate));
            // definition.setFilters(Arrays.asList(filterDefinition));
            definition.setUri(uri);
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        });
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

    @Override
    public void deleteRoute(String routeId) {
        routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
    }

    private ApplicationEventPublisher applicationEventPublisher;

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void run(String... args) {
        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载路由数据等操作<<<<<<<<<<<<<");
        this.refreshGatewayRouteIntoRouteDefinitionWriter();
    }
}

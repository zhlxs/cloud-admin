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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HSL
 * @date 2021-11-19 0:14
 * @desc
 */
@Slf4j
@Service("sysRouteConfService")
public class SysRouteConfServiceImpl implements SysRouteConfService, ApplicationEventPublisherAware, CommandLineRunner {

    @Autowired
    private GatewayDao gatewayDao;
    @Autowired
    private RedisRouteDefinitionRepository routeDefinitionWriter;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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

    public void saveRoute(GatewayDTO gatewayRoute) {
        RouteDefinition definition = handleRouteData(gatewayRoute);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void update(GatewayDTO gatewayRoute) {
        RouteDefinition definition = handleRouteData(gatewayRoute);
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            log.error("更新路由异常：{}", e);
        }
    }


    /**
     * @Author: HSL
     * @Date: 2021/11/19 13:47
     * @Desc: 刷新路由信息
     **/
    @Override
    public String refreshGatewayRouteIntoRouteDefinitionWriter() {
        log.info("====开始加载=====网关配置信息=========");
        //删除redis里面的路由配置信息
        redisTemplate.delete(RedisRouteDefinitionRepository.GATEWAY_ROUTES);
        List<GatewayDTO> gatewayList = findAllRoutes();
        log.info("网关配置信息：=====>" + JSON.toJSONString(gatewayList));
        gatewayList.forEach(route -> {
            RouteDefinition definition = handleRouteData(route);
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        });
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

    /**
     * @author HSL
     * @date 2021-12-13
     * @desc 构建路由数据
     **/
    private RouteDefinition handleRouteData(GatewayDTO route) {
        RouteDefinition definition = new RouteDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        PredicateDefinition predicate = new PredicateDefinition();
        FilterDefinition filterDefinition = new FilterDefinition();
        Map<String, String> filterParams = new HashMap<>(8);
        URI uri = null;
        if (route.getUri().startsWith("http")) {
            //http地址
            uri = UriComponentsBuilder.fromHttpUrl(route.getUri()).build().toUri();
        } else {
            //注册中心
            uri = UriComponentsBuilder.fromUriString("lb://" + route.getUri()).build().toUri();
        }
        definition.setId(route.getRouteId());
        // 名称是固定的，spring gateway会根据名称找对应的PredicateFactory
        predicate.setName("Path");
        predicateParams.put("pattern", route.getPredicates());
        predicate.setArgs(predicateParams);
        // 名称是固定的, 路径去前缀
        filterDefinition.setName("StripPrefix");
        filterParams.put("_genkey_0", route.getFilters().toString());
        filterDefinition.setArgs(filterParams);
        definition.setPredicates(Arrays.asList(predicate));
        definition.setFilters(Arrays.asList(filterDefinition));
        definition.setUri(uri);
        definition.setOrder(route.getOrderno());
        return definition;
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

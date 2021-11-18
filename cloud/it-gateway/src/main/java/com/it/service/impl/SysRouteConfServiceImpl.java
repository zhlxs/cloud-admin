package com.it.service.impl;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.it.dao.GatewayDao;
import com.it.entity.GatewayDTO;
import com.it.service.SysRouteConfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author HSL
 * @date 2021-11-19 0:14
 * @desc
 */
@Service("sysRouteConfService")
@Slf4j
public class SysRouteConfServiceImpl implements SysRouteConfService {

    @Autowired
    private GatewayDao gatewayDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<GatewayDTO> findAllRoutes() {
        return gatewayDao.selectList(new QueryWrapper<>());
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
}

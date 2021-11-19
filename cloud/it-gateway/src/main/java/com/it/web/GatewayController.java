package com.it.web;

import com.it.entity.ApiResult;
import com.it.entity.GatewayDTO;
import com.it.service.SysRouteConfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author HSL
 * @date 2021-11-19 13:55
 * @desc 网关服务
 */
@Slf4j
@Api(tags = "网关服务")
@Validated
@RestController
@RequestMapping(value = "/gateway")
public class GatewayController {

    @Autowired
    private SysRouteConfService sysRouteConfService;

    @ApiOperation(value = "查询全部路由")
    @GetMapping("/findAllRoutes")
    ApiResult<List<GatewayDTO>> findAllRoutes() {
        return ApiResult.successDef(sysRouteConfService.findAllRoutes());
    }

    @ApiOperation(value = "刷新路由")
    @GetMapping("/refreshRoute")
    ApiResult<String> refreshRoute() {
        return ApiResult.successDef(sysRouteConfService.refreshGatewayRouteIntoRouteDefinitionWriter());
    }

    @ApiOperation(value = "新增路由")
    @PostMapping("/refreshRoute")
    ApiResult<String> createRoute(@RequestBody GatewayDTO dto) {
        return ApiResult.successDef(sysRouteConfService.createRoute(dto));
    }
}

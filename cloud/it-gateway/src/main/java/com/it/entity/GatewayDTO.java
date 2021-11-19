package com.it.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author HSL
 * @date 2021-11-18 23:37
 * @desc 网关对象
 */
@Data
@ApiModel(description = "网关对象")
@TableName("gateway")
public class GatewayDTO implements Serializable {
    private static final long serialVersionUID = -5504186323611389491L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("路由id")
    @NotEmpty(message = "服务名称不能为空")
    private String routeId;

    @ApiModelProperty("断言")
    private String predicates;

    @ApiModelProperty("过滤")
    private String filters;

    @ApiModelProperty("路由地址")
    private String uri;

    @ApiModelProperty("排序码")
    private Integer order;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("更新时间")
    private String updateTime;

    @ApiModelProperty("删除标识")
    private Integer delFlag;
}

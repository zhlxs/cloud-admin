package com.it.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.GatewayDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GatewayDao extends BaseMapper<GatewayDTO> {

}

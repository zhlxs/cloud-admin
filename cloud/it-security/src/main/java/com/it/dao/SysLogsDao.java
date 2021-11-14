package com.it.dao;

import com.it.entity.SysLogs;
import com.it.entity.SysLogsDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysLogsDao {

    @Insert("insert into sys_logs(userId, module, flag, remark, createTime) values(#{user.id}, #{module}, #{flag}, #{remark}, now())")
    int save(SysLogs sysLogs);

    int count(@Param("params") Map<String, Object> params);

    List<SysLogsDO> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Delete("delete from sys_logs where createTime <= #{time}")
    int deleteLogs(String time);
}

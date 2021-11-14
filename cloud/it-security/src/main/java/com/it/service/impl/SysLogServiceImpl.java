package com.it.service.impl;

import com.it.dao.SysLogsDao;
import com.it.entity.SysLogs;
import com.it.entity.SysUser;
import com.it.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author HSL
 * @date 2021-11-13 16:30
 * @desc 系统事务
 */
@Service
@Slf4j
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogsDao sysLogsDao;

    /**
     * @Author: HSL
     * @Date: 2021/11/13 16:40
     * @Desc: 保存日志
     **/
    @Async
    @Override
    public void save(SysLogs sysLogs) {
        if (sysLogs == null || sysLogs.getUser() == null || sysLogs.getUser().getId() == null) {
            return;
        }
        sysLogsDao.save(sysLogs);
    }

    /**
     * @Author: HSL
     * @Date: 2021/11/13 16:40
     * @Desc: 保存日志
     **/
    @Async
    @Override
    public void save(Long userId, String module, Boolean flag, String remark) {
        SysLogs sysLogs = new SysLogs();
        sysLogs.setFlag(flag);
        sysLogs.setModule(module);
        sysLogs.setRemark(remark);
        // 操作用户
        SysUser user = new SysUser();
        user.setId(userId);
        sysLogs.setUser(user);
        sysLogsDao.save(sysLogs);
    }

    /**
     * @Author: HSL
     * @Date: 2021/11/13 16:40
     * @Desc: 删除日志
     **/
    @Override
    public void deleteLogs() {
        // 清除3个月之前的日志
        Date date = DateUtils.addMonths(new Date(), -3);
        String time = DateFormatUtils.format(date, DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern());
        int n = sysLogsDao.deleteLogs(time);
        log.info("删除{}之前日志{}条", time, n);
    }
}

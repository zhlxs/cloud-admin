package com.it.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.it.annotation.GenKey;
import com.it.constant.TableConstant;

@TableName(TableConstant.SYS_USER)
@GenKey(value = "id", tabName = "sys_user")
public class SysUserDO extends SysUser
{
	private static final long serialVersionUID = -6525908145032868837L;
}

package com.it.entity;

import com.it.annotation.GenKey;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseEntity<T extends Serializable> implements Serializable
{

	private static final long serialVersionUID = 2054813493011812469L;

	protected T id;
	private Date createTime = new Date();
	private Date updateTime = new Date();

	public T getId()
	{
		return id;
	}

	public void setId(T id)
	{
		this.id = id;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
}

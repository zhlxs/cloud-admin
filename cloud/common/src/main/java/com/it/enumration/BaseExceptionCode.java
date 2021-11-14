package com.it.enumration;

public interface BaseExceptionCode
{
	String getCode();

	String getMsg();

	default int getStatus()
	{
		return 500;
	}
}

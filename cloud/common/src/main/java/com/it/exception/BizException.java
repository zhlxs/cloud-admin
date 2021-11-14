package com.it.exception;

import com.it.enumration.BaseExceptionCode;

public class BizException extends BaseUnCheckedException
{
	private static final long serialVersionUID = -1L;

	public BizException(String message)
	{
		super("1", message);
	}

	public BizException(String code, String message, Object... args)
	{
		super(code, message, args);
	}

	public static BizException wrap(BaseExceptionCode ex)
	{
		return new BizException(ex.getCode(), ex.getMsg(), new Object[0]);
	}

	public static BizException wrap(String code, String message, Object... args)
	{
		return new BizException(code, message, args);
	}

	@Override
	public String toString()
	{
		return "BizException{" + "message='" + message + '\'' + ", code='" + code + '\'' + '}';
	}
}

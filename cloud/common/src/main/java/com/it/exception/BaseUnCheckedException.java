package com.it.exception;

import com.it.enumration.ExceptionCode;

public class BaseUnCheckedException extends RuntimeException implements BaseException
{
	private static final long serialVersionUID = -1L;
	protected String message;
	protected String code;

	public BaseUnCheckedException(String code, String message)
	{
		super(message);
		this.code = code;
		this.message = message;
	}

	public BaseUnCheckedException(String code, String format, Object... args)
	{
		super(String.format(format, args));
		this.code = code;
		this.message = String.format(format, args);
	}

	public BaseUnCheckedException(String message, Throwable throwable)
	{
		super(message,throwable);
		this.code = ExceptionCode.HTTP_STATUS_INTERNAL_SERVER_ERROR.getCode();
		this.message =message;
	}

	@Override
	public String getCode()
	{
		return this.code;
	}

	@Override
	public String getMessage()
	{
		return this.message;
	}
}

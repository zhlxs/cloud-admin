package com.it.enumration;

public enum ExceptionCode implements BaseExceptionCode
{
	HTTP_STATUS_INTERNAL_SERVER_ERROR("内部服务错误", 500),
	OPERATION_EX("操作异常");
	private String code;
	private String msg;
	private Integer status = 500;

	ExceptionCode(String msg)
	{
		this.msg = msg;
		this.status = 500;
	}

	ExceptionCode(String msg, int status)
	{
		this.msg = msg;
		this.status = status;
	}

	ExceptionCode(Object code, String msg)
	{
		this.code = code == null ? null : String.valueOf(code);
		this.msg = msg;
	}

	@Override
	public String getCode()
	{
		return this.code;
	}

	@Override
	public String getMsg()
	{
		return this.msg;
	}

	@Override
	public int getStatus()
	{
		return this.status;
	}
}

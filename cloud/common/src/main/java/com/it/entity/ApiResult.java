package com.it.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class ApiResult<T> implements Serializable
{
	public static final String CODE = "code";
	public static final String MSG = "msg";
	private static final long serialVersionID = -1L;

	private String code;
	private String msg;
	private T data;

	private String path;

	public ApiResult()
	{
		this.code = "1";
		this.msg = "success";
	}

	public ApiResult(T data)
	{
		this.code = "1";
		this.msg = "success";
		this.data = data;
	}

	public ApiResult(String code, String message)
	{
		this.code = "1";
		this.msg = "success";
		this.code = code;
		this.msg = message;
	}

	public ApiResult(String code, String message, T data)
	{
		this.code = code;
		this.msg = message;
		this.data = data;
	}

	public ApiResult(Integer code, String message)
	{
		this(String.valueOf(code), message);
	}

	public boolean isSuccess()
	{
		return "1".equals(this.code);
	}

	public String toString()
	{
		return JSONObject.toJSONString(this);
	}

	public static <E> ApiResult<E> success(E data, String msg)
	{
		return new ApiResult<E>("1", msg, data);
	}

	public static <E> ApiResult<E> success(E data)
	{
		return new ApiResult<>("1", null, data);
	}

	public static <E> ApiResult<E> fail(String code, String msg)
	{
		return new ApiResult<>(code, msg != null && msg.isEmpty() ? msg : "系统繁忙，请稍后再试");
	}

	public static <E> ApiResult<E> fail(String msg)
	{
		return fail("-1", msg);
	}

	public static <E> ApiResult<E> fail(String msg, Object... args)
	{
		String message = msg != null && msg.isEmpty() ? msg : "系统繁忙，请稍后再试";
		return new ApiResult<>("-1", String.format(message, args));
	}

	public static <E> ApiResult<E> fail(Throwable throwable)
	{
		return fail("-1", throwable != null ? throwable.getMessage() : "系统繁忙，请稍后再试");
	}

	public static <E> ApiResult<E> result(String code, String msg, E data)
	{
		return new ApiResult<>(code, msg, data);
	}

	public static <E> ApiResult<E> successDef(E data)
	{
		return new ApiResult<>("1", null, data);
	}

	public static <E> ApiResult<E> successDef()
	{
		return new ApiResult<>("1", null, null);
	}

	public String getCode()
	{
		return code;
	}

	public ApiResult<T> setCode(final String code)
	{
		this.code = code;
		return this;
	}

	public String getMsg()
	{
		return msg;
	}

	public ApiResult<T> setMsg(final String msg)
	{
		this.msg = msg;
		return this;
	}

	public T getData()
	{
		return data;
	}

	public ApiResult<T> setData(final T data)
	{
		this.data = data;
		return this;
	}

	public String getPath()
	{
		return path;
	}

	public ApiResult<T> setPath(final String path)
	{
		this.path = path;
		return this;
	}
}

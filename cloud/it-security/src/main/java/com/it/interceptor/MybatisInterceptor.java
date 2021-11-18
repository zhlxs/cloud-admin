package com.it.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.it.annotation.GenKey;
import com.it.entity.SysUserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// 拦截mybatis的insert语句，使用自定义策略生成主键
@Component
@Intercepts(@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }))
@Slf4j
public class MybatisInterceptor implements Interceptor
{
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Object intercept(Invocation invocation) throws Throwable
	{
		Object[] args = invocation.getArgs();
		MappedStatement statement = (MappedStatement) args[0];
		if (SqlCommandType.INSERT.name().equals(statement.getSqlCommandType().name()))
		{
			Object dataObject = args[1];
			Class<?> clazz = dataObject.getClass();
			GenKey genKey = clazz.getAnnotation(GenKey.class);
			if (ObjectUtil.isNotNull(genKey))
			{
				String getName = "get" + genKey.value().substring(0, 1).toUpperCase() + (genKey.value().length() > 1 ? genKey.value().substring(1) : "");
				Method m = clazz.getMethod(getName);
				Object value = m.invoke(dataObject);
				if (ObjectUtil.isNull(value))
				{
					// 调用服务请求id
					String tabName = genKey.tabName();
					String result = restTemplate.getForObject("http:192.168.1.102:8080/api/segment/get/" + tabName, String.class);
					Long nextId = Long.valueOf(result);
					PropertyDescriptor pd = new PropertyDescriptor(genKey.value(), dataObject.getClass());
					Method mm = pd.getWriteMethod();
					mm.invoke(dataObject, nextId);
				}
			}
		}
		// for (Object o : args)
		// {
		// 	if (o instanceof MappedStatement)
		// 	{
		// 		MappedStatement statement = (MappedStatement) o;
		// 		if (SqlCommandType.INSERT.equals(statement.getSqlCommandType()))
		// 		{
		// 			ParameterMap params = ((MappedStatement) o).getParameterMap();//其中包含类信息
		// 			Class<?> clazz = params.getType();// 类对象
		// 			List<Field> fieldList = new ArrayList<>();
		// 			while (clazz != null){
		// 				fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
		// 				clazz = clazz.getSuperclass();
		// 			}
		// 			Field[] fields = new Field[fieldList.size()];
		// 			fieldList.toArray(fields);
		// 			Class<?> superclass = params.getType().getClass();
		// 			for (Field field : fields){
		// 				if (field.getAnnotation(GenKey.class) != null){
		// 					field.setAccessible(true);
		// 					String name = field.getName();
		// 					String getMethod = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);

		// 					Object invoke = superclass.getMethod(getMethod).invoke(superclass);
		// 					System.out.println(field.get(params.getType()));
		// 				}
		// 			}
		// 			return invocation.proceed();
		// 		}
		// 	}
		// else
		// {
		// 	// ParameterMap params = ((MappedStatement) o).getParameterMap();//其中包含类信息
		// 	// Class<?> type = params.getType();// 类对象
		// 	Field[] fields = o.getClass().getDeclaredFields();
		// 	for (Field field : fields)
		// 	{
		// 		if (field.getAnnotation(GenKey.class) != null)
		// 		{
		// 			// 主键已生成不再处理
		// 			field.setAccessible(true);
		// 			if (field.get(o) != null)
		// 			{
		// 				return invocation.proceed();
		// 			}
		// 			// 生成主键并且赋值
		// 			String name = field.getName();
		// 			String setMethod = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
		// 			Long primaryKey = 100L;
		// 			o.getClass().getMethod(setMethod, Long.class).invoke(o, primaryKey);
		// 			return invocation.proceed();
		// 		}
		// 	}
		// }
		//		}
		return invocation.proceed();
	}

//	public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
//	{
//		SysUserDO sysUserDO = new SysUserDO();
//		Method m = sysUserDO.getClass().getMethod("getId");
//		Object o = m.invoke(sysUserDO);
//		Method m1 = sysUserDO.getClass().getMethod("setId");
//		sysUserDO.getClass().getMethod("setId", new Class[] { Long.class }).invoke(sysUserDO, new Object[1]);
//		System.out.println(o);
//		System.out.println(sysUserDO.getId());
//	}
}

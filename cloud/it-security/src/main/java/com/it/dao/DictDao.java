package com.it.dao;

import com.it.entity.DictDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DictDao
{

	@Select("select * from t_dict t where t.id = #{id}")
	DictDO getById(Long id);

	@Delete("delete from t_dict where id = #{id}")
	int delete(Long id);

	int update(DictDO dict);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into t_dict(type, k, val, createTime, updateTime) values(#{type}, #{k}, #{val}, now(), now())")
	int save(DictDO dict);

	int count(@Param("params") Map<String, Object> params);

	List<DictDO> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

	@Select("select * from t_dict t where t.type = #{type} and k = #{k}")
	DictDO getByTypeAndK(@Param("type") String type, @Param("k") String k);

	@Select("select * from t_dict t where t.type = #{type}")
	List<DictDO> listByType(String type);
}

package com.efuture.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.efuture.domain.User;

@Mapper
public interface UserDao {

	@Select("select * from users where id = #{id}")
	public User getById(@Param("id") Long id);

	@Insert("insert into users(id, name)values(#{id}, #{name})")
	public int insert(User user);

	@Update("update users set password = #{password} where id = #{id}")
	public void update(User toBeUpdate);
}

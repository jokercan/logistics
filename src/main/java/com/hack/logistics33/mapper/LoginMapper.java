package com.hack.logistics33.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LoginMapper {
    @Select("Select password from person where id = #{id}")
    public String selectPasswd(@Param("id") String id) throws Exception;

    @Select("Select if(Exists(Select * from person where id = #{id} ) , TRUE , FALSE)AS judge")
    public boolean checkExistUser(@Param("id") String id) throws Exception; // 判断用户是否存在

    @Update("Update person set token = #{token} where id = #{id}")
    public void updateToken(@Param("token") String token, @Param("id") String id) throws  Exception;
}

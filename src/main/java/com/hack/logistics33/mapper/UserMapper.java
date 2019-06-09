package com.hack.logistics33.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("Select if(Exists(Select * from person where token = #{token}) , TRUE , FALSE)AS judge")
    public boolean checkToken(@Param("token") String token) throws Exception;

    @Select("Select id from person where token = #{token}")
    public String selectIdByToken(@Param("token") String token)  throws Exception;
}

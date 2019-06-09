package com.hack.logistics33.mapper;


import com.hack.logistics33.domain.Info;
import com.hack.logistics33.domain.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OperationMapper {
    @Select("Select * from product")
    public List<Product> selectAllProduct() throws Exception;

    @Select("Select * from product order by CONVERT(${coloum} using gbk) desc")
    public List<Product> selectAllProductDesc(@Param("coloum") String coloum) throws Exception; //降序

    @Select("Select * from product order by CONVERT(${coloum} using gbk) asc")
    public List<Product> selectAllProductAsc(@Param("coloum") String coloum) throws Exception;  //升序

    @Select("Select * from product order by ${coloum} desc")
    public List<Product> selectNumberAllProductDesc(@Param("coloum") String coloum) throws Exception; //降序

    @Select("Select * from product order by ${coloum} asc")
    public List<Product> selectNumberAllProductAsc(@Param("coloum") String coloum) throws Exception;  //升序

    @Update("Update product set type = #{type} , price = #{price} , count = #{count} , name = #{name} , trademark = #{trademark} where id = #{id}")
    public void changeProduct
            (@Param("type") String type, @Param("price") double price,
             @Param("count") int count, @Param("name") String name, @Param("trademark") String trademark,
             @Param("id") int id) throws Exception;

    @Insert("insert into info(record , operator , time) values (#{record} , #{operator} , now()) ")
    public void insertChangeInfo(@Param("record") String record, @Param("operator") String operator) throws Exception;

    @Delete("delete from product where id = #{id}")
    public void deleteProduct(@Param("id") int id) throws Exception;

    @Select("select name from product where id = #{id}")
    public String selectNameById(@Param("id") int id) throws Exception;

    @Select("Select if(Exists(Select * from product where id = #{id}) , TRUE , FALSE)AS judge")
    public boolean checkExistProduct(@Param("id") int id);

    @Select("Select count from product where id = #{id}")
    public int selectPCount(@Param("id") int id) throws Exception;

    @Update("Update product set count = #{count} where id = #{id}")
    public void updateP(@Param("count") int count , @Param("id")int id) throws Exception;

    @Insert("Insert into product(type , name , trademark , price , count) values (#{type} " +
            ", #{name} , #{trademark} , #{price} , #{count})")
    public void insertProduct(@Param("type") String type, @Param("name") String name, @Param("trademark") String trademark,
                              @Param("price") double price, @Param("count") int count) throws Exception;

    @Select("Select * from product where type like #{content}")
    public List<Product> selectSpecialPType(@Param("content") String content) throws Exception;

    @Select("Select * from product where name like #{content}")
    public List<Product> selectSpecialPName(@Param("content") String content) throws Exception;

    @Select("Select * from product where trademark like #{content}")
    public List<Product> selectSpecialPTrademark(@Param("content") String content) throws Exception;

    @Select("Select * from product where price like #{content}")
    public List<Product> selectSpecialPPrice(@Param("content") String content) throws Exception;

    @Select("Select * from product where count like #{content}")
    public List<Product> selectSpecialPCount(@Param("content") String content) throws Exception;

    @Select("Select * from info order by time desc")
    public List<Info> selectAllInfo() throws Exception;

    @Select("Select if(Exists(Select * from info where id = #{id}) , TRUE , FALSE)AS judge")
    public boolean checkExistInfo(@Param("id") int id) throws Exception;

    @Delete("delete from info where id = #{id}")
    public void deleteInfo(@Param("id") int id) throws Exception;

    @Select("Select if(Exists(Select * from product where type = #{type} and name = #{name} and trademark = #{trademark} and price = #{price}) , TRUE , FALSE)AS judge")
    public boolean checkExistMany(@Param("type")String type , @Param("name")String name , @Param("trademark")String trademark , @Param("price")double price) throws  Exception;

    @Update("Update product set count = count + #{count} where type = #{type} and name = #{name} and trademark = #{trademark} and price = #{price}")
    public void updateExistP(@Param("count") int count ,
                             @Param("type")String type ,
                             @Param("name")String name ,
                             @Param("trademark")String trademark,
                             @Param("price")double price)throws Exception;
}

package com.qingfeng.shop.repository;


import com.qingfeng.shop.bean.User;
import org.apache.ibatis.annotations.Select;

/**
 * UserMapper 数据访问类
 *  //到userMapper数据访问层持久层对象中创建要登录的对象
 */
public interface UserMapper {

    @Select("select * from ec_user where login_name = #{dfff}")
    User login(String loginName);
}
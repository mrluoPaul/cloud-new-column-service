package com.mrluo.cloud.modules.app.mapper;

import com.mrluo.cloud.modules.app.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-21 10:10:51
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User queryUserByUserNameOrEmail(@Param("userName") String userName);
}
package com.tiantian.mapper;

import com.tiantian.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * description:
 */
@Mapper
public interface UserMapper {
    User findOne();

    User findById(Integer id);
}

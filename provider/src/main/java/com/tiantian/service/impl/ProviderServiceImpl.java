package com.tiantian.service.impl;

import com.tiantian.entity.User;
import com.tiantian.mapper.UserMapper;
import com.tiantian.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description:
 */
@Service
public class ProviderServiceImpl implements ProviderService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findOne() {
        return userMapper.findOne();
    }

    @Override
    public User id(Integer id) {
        return userMapper.findById(id);
    }
}

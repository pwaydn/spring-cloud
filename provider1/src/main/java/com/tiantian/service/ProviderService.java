package com.tiantian.service;

import com.tiantian.entity.User;

/**
 * description:
 */
public interface ProviderService {
    User findOne();

    User id(Integer id);
}

package com.tiantian.fallback;

import com.tiantian.entity.User;
import com.tiantian.feign.ConsumerService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * description:
 */
@Component
public class FallbackA implements FallbackFactory<ConsumerService>{
    @Override
    public ConsumerService create(Throwable throwable) {
        return new ConsumerService() {
            @Override
            public User findById(Integer id) {
                return new User().setNickname("服务熔断客户端降级信息！").setId(id);
            }

            @Override
            public User findOne() {
                return null;
            }
        };
    }
}

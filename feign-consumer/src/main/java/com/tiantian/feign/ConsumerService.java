package com.tiantian.feign;

import com.tiantian.entity.User;
import com.tiantian.fallback.FallbackA;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * description:
 */
@FeignClient(value = "feignprovider18081", fallbackFactory = FallbackA.class)
public interface ConsumerService {


    @RequestMapping(value = "/feignProvider/id/{id}", method = RequestMethod.GET)
    public User findById(@PathVariable("id") Integer id);

    @RequestMapping(value = "/feignProvider/findOne", method = RequestMethod.GET)
    public User findOne();
}
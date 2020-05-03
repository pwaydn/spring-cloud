package com.tiantian.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tiantian.entity.User;
import com.tiantian.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * description:
 */
@RestController
@RequestMapping("/feignProvider")
public class FeignProviderController {
    @Autowired
    private ProviderService providerService;

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public User findById(@PathVariable("id") Integer id) {
        return providerService.id(id);
    }



    @RequestMapping(value = "/findOne", method = RequestMethod.GET)
    public User findOne() {
        return providerService.findOne();
    }
}

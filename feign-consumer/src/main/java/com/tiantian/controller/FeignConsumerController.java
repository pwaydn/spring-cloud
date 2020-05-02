package com.tiantian.controller;

import com.tiantian.entity.User;
import com.tiantian.feign.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * description:
 */
@RestController
@RequestMapping("/fegin")
public class FeignConsumerController {
    @Autowired
    private ConsumerService consumerService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User findById(@PathVariable("id") Integer id){
        return consumerService.findById(id);
    }

    @RequestMapping(value = "/findOne", method = RequestMethod.GET)
    public User findOne(){
        return consumerService.findOne();
    }
}

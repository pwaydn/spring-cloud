package com.tiantian.controller;

import com.tiantian.config.ConfigBean;
import com.tiantian.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * description:
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {
//    private static final String RestUrlPrefix = "http://localhost:18081";
    private static final String RestUrlPrefix = "http://providers";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/findOne")
    public User id() {
        return restTemplate.getForObject(RestUrlPrefix +  "/provider/findOne",User.class);
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public User id(@PathVariable Integer id) {
        return restTemplate.getForObject(RestUrlPrefix + "/provider/id/" + id,User.class);
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUser(@RequestBody User user){
        return restTemplate.postForObject(RestUrlPrefix + "/provider/addUser",user,String.class);
    }
}

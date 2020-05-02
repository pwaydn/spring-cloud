package com.tiantian.controller;

import com.tiantian.entity.User;
import com.tiantian.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * description:
 */
@RestController
@RequestMapping("/provider")
public class ProviderController {
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

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUser(@RequestBody User user) {

        return user.getUsername();
    }
}

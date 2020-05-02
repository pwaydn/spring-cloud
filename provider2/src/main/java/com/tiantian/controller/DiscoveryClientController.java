package com.tiantian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description:
 */
@RestController
@RequestMapping("/discovery")
public class DiscoveryClientController {
    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/name/{name}")   // provider18081
    public Object getServer(@PathVariable String name) {
        List<String> services = client.getServices();
        System.out.println(services);

        List<ServiceInstance> instances = client.getInstances(name);
        for (ServiceInstance instance : instances) {
            System.out.println(instance.getInstanceId());
            System.out.println(instance.getHost());
            System.out.println(instance.getPort());
            System.out.println(instance.getUri());
        }
        return client;
    }

}

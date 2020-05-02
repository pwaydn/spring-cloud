# SpringCloud

# 一、微服务概述

## 1. 微服务

​	微服务强调的是服务的大小，是具体解决某一问题对应的一个服务应用(一个模块)。

​	优点：服务内聚、小，开发简单、效率高，松耦合，容易被一个开发人员理解修改和维护，前后端分离。

​	缺点：开发人员要处理分布式系统的复杂性，服务增多时运维压力增大，通信成本，系统集成测试更难等。

##2. 微服务架构

​	微服务架构是一种架构模式(或者说一种架构风格)，他提倡将单一应用程序划分为一组小的服务，彻底去耦合，服务进程相互独立，相互协调，互相配合共同实现一个完整功能。服务之间用轻量级的通信机制互相沟通，每个服务都围绕着具体的业务进行构建，并能独立的部署到生产环境。

​	微服务架构强调的是一个整体，多个微服务组装拼接成一个整体对外服务。

## 3. SpringCloud与Dubbo

​	Dubbo使用的是远程调用协议RPC，SpringCloud是基于HTTP的REST风格。目前dubbo和SpringCloud只能二选一，Dubbo正在寻求适配到SpringCloud生态。

​	SpringCloud中国社区：http://springcloud.cn/

​	SpringCloud中文网：https://www.springcloud.cc/

## 4. SpringCloud

​	SpringCloud是微服务的一种实现，是分布式架构下的一站式解决方案，是微服务架构落地技术的集合体。SpringCloud依赖SpringBoot，SpringCloud将SpringBoot开发的单体微服务整合起来，为微服务之间提供服务发现、配置和路由等各种功能。

	1. 服务注册与发现：Eureka [juˈriːkə]    [ j ]音标类似于 ye的发音
 	2. 负载均衡：Ribbon  [ˈrɪbən] 客户端负载均衡
 	3. 负载均衡：Feign   [feɪn] 

#二、SpringCloud工程

## 1. 创建父工程

1. 打包方式pom
2. 依赖管理导入SpringBoot和SpringCloud

注意版本号的对应问题，去 SpringCloud官网查看下

```
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>2.2.1.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>Hoxton.SR4</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

##2. 服务注册中心Eureka

​	Eureka是一个基于Rest的服务，用户服务注册与发现，他通过服务标识符就可访问到服务。Eureka包含两个组件，EurekaServer和EurekaClient。

​	EurekaClient：一个java客户端，同时内置一个负载均衡器，应用启动后会向EurekaServer发送心跳，默认周期30秒，超过90秒EurekaServer会将节点从注册表移除。

​	EurekaServer：服务启动后会在EurekaServer进行注册，EurekaServer会存储所有可用节点信息，系统中的其他服务通过EurekaClient连接到EurekaServer并维持心跳连接，运维可通过EurekaServer监控各服务运行状态。

​	CPA：一致性（Consistency）、可用性（Availability）、分区容错性（Partition tolerance）分布式只能同时满足两个，P分区容错是要满足的，因此只能是AP和CP组合。

​	Eureka与Zookeeper： Eureka遵守AP，Zookeeper遵守CP，Zookeeper重新选举时系统是瘫痪的。Eureka能更好的应对因网络故障导致的部分节点失去联系的情况，不会使Zookeeper一样使整个系统瘫痪。

###1）服务注册中心

	1. 导入坐标

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

	2. 配置文件application.yml

```
server:
  port: 17001
eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false    # 不注册
    fetch-registry: false          # 不扫描
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/  # 注册中心
  server:
    enable-self-preservation: true    # 自我保护机制 默认true 保留暂时失联的服务，随心跳恢复而恢复
```

3. 启动引导类

```
@SpringBootApplication
@ComponentScan("com.tiantian")
@EnableEurekaServer
public class EurekaServer17001 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer17001.class,args);
    }
}
```

### 2）服务注册

1. 导入依赖

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

2. 配置application.yml

```
eureka:
  client:
    service-url:
      defaultZone: http://localhost:17001/eureka/   # 单机交互地址集群交互地址
  instance:
      prefer-ip-address: true                       # 配置status那块的地址信息显示IP
      instance-id: server-8006                      # 配置服务status那块的名字
```

3. 开启组件支持注解启动类添加@EnableEurekaClient，服务启动后自动注册

```
@SpringBootApplication
@ComponentScan("com.tiantian")
@MapperScan("com.tiantian.mapper")
@EnableEurekaClient
public class ProviderApplication18081 {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication18081.class,args);
    }
}
```

4. 完善微服务info信息

   1. 导入坐标

      ```
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-actuator</artifactId>
      </dependency>
      ```

   2. 配置maven的resource插件

      ```
      <build>
          <finalName>spring-cloud</finalName>
          <resources>
              <resource>
                  <directory>src/main/resources</directory>
                  <filtering>true</filtering>
              </resource>
          </resources>
          <plugins>
              <plugin>
                  <groupId>org.apache.maven.plugins</groupId>
                  <artifactId>maven-resources-plugin</artifactId>
                  <configuration>
                      <delimiters>
                          <delimiter>$</delimiter>
                      </delimiters>
                  </configuration>
              </plugin>
          </plugins>
      </build>
      ```

   3. 配置文件中配置服务信息

      ```
      info:
        app.name: provider project
        company.name: www.tiantian.com
        build.artifactId: $project.artifactId$
        build.version: $project.version$
      ```

### 3）服务发现

1. 启动类上添加@EnableDiscoveryClient

2. 类中注入接口DiscoveryClient获取信息

   ```
   @Autowired
   private DiscoveryClient client;
   // 获取所有服务名
   List<String> services = client.getServices();
   // 根据服务名获取实例，一个服务可有多个实例
   List<ServiceInstance> instances = client.getInstances(name);
   // 获取服务名
   instance.getInstanceId();  
   // 获取服务IP
   instance.getHost();
   // 获取服务端口
   instance.getPort();
   // 服务地址
   instance.getUri();
   ```

### 4）EurekaServer集群

 1. 修改EurekaServer的application，defaultZone配置除本机节点外的其他节地址

    ```
    defaultZone: http://localhost:19002/eureka/,http://localhost:19003/eureka/
    ```

	2. 修改服务注册地址

    ```
    http://tian1.com:19001/eureka/,http://tian2.com:19002/eureka/
    ```

##3. 负载均衡Ribbon

​	Ribbon是客户端负载均衡工具，主要提供客户端软件负载均衡算法，可用自定义负载均衡算法。消费方从注册中心获取有哪些服务可用，然后选择一个合适的服务器。

1. 在消费端添加坐标

   ```
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
   </dependency>
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-config</artifactId>
   </dependency>
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
   </dependency>
   ```

2. 在客户端RestTemplate上添加注解@LoadBalanced

   ```
   @LoadBalanced
   public RestTemplate restTemplate() {
       return new RestTemplate();
   }
   ```

3. 启动引导类添加@EnableEurekaClient

4. 客户端Rest调用接口中的IP和端口信息改为微服务的名字，此时通可过微服务名调用，不再关注IP和端口

   ```
   restTemplate.getForObject("http://localhost:18081/provider/id/" + id,User.class);
   restTemplate.getForObject("http://providers18081/provider/id/" + id,User.class);
   ```

5. 服务端需要有统一的服务名，客户端通过服务名调用，此时是默认的轮询负载均衡算法

6. 使用Ribbon定义的算法，在配置文件中注入指定算法的Bean即可，定义好的算法有7种

   ```
   @Bean
   public IRule iRule() {
   	return new RoundRobinRule();  // 默认的轮询算法
   }
   RandomRule  //随机算法
   RetryRule   //若出现多次故障则跳过此服务
   ```

7. 自定义负载均衡算法

   1. 启动类添加@RibbonClient(name="providers",configuration = MyRule.class)

      指定服务名，指定自定义配置类，**自定义的配置类不能在包扫描下**。

      拷贝github的算法类源码，修改成自己的 ，返回即可. . . 

      ```
      @Configuration
      public class MyRule {
          @Bean
          public IRule iRule() {
              return new MyRule();   // MyRule是自定义的规则类
          }
      }
      ```

## 4. feign远程调用

​	之前调用服务端方法是通过RestTemplate的方式，而Feign则在此基础上做了进一步的封装，简化了开发。

 1. 服务端开发

    注册服务即可

	2. 消费端

    	1. 启动类添加注解

    ```
    @EnableEurekaClient
    @EnableFeignClients("com.tiantian.feign")
    ```

    2. 服务调用接口，指定服务名和方法对应的全路径，@PathVariable必须有别名

    ```
    @FeignClient("feignprovider18081")
    public interface ConsumerService {
    
        @RequestMapping(value = "/provider/id/{id}", method = RequestMethod.GET)
        public User findById(@PathVariable("id") Integer id);
    }
    ```

    3. 在Controller中注入接口调用类调用方法
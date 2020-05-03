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
   	4. 熔断器：Hystrix
   	5. 仪表盘：HystrixDashboard  监控

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

## 4. 远程调用feign

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

## 5. 熔断器Hystrix

​	微服务架构中，服务端程序异常，长时间没回应，会造成服务雪崩，导致更多级联故障，因此需要对故障隔离处理，以便单个应用失败，不再影响整个系统的运行。

​	熔断器在服务单元发生故障后，通过故障监控，会返回调用方一个可处理的备选响应(FallBack)，保证服务调用方线程不被不必要的占用，从而避免整个系统的雪崩。熔断器默认5秒内20次调用失败就会启动熔断机制。

1. 服务端导入熔断器坐标

   ```
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
   </dependency>
   ```

2. 开启熔断器

   启动引导类添加注解@EnableCircuitBreaker

3. 1 在服务端定义熔断： 定义熔断方法，并在方法上指定

   ```
   @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
   @HystrixCommand(fallbackMethod = "hystrix")
   public User findById(@PathVariable("id") Integer id) {
       return providerService.id(id);
   }
   
   public User hystrix(@PathVariable("id") Integer id) {
       return new User().setNickname("服务熔断！").setId(id);
   }
   ```

3. 2 在客户端定义熔断：配置文件开启熔断器，定义熔断方法类，在Feign调用服务接口指定熔断工厂

   ```
   feign:
     hystrix:
       enabled: true
   ```

   ```
   @Component
   public class FallbackA implements FallbackFactory<ConsumerService>{
       @Override
       public ConsumerService create(Throwable throwable) {
           return new ConsumerService() {
               @Override
               public User findById(Integer id) {
                   return null;
               }
   
               @Override
               public User findOne() {
                   return null;
               }
           }
       }
   }
   
   @FeignClient(value = "feignprovider18081", fallbackFactory = FallbackA.class)
   public interface ConsumerService {
       @RequestMapping(value = "/feignProvider/id/{id}", method = RequestMethod.GET)
       public User findById(@PathVariable("id") Integer id);
   
       @RequestMapping(value = "/feignProvider/findOne", method = RequestMethod.GET)
       public User findOne();
   }
   ```

##6. 仪表盘HystrixDashboard（2.x版本未测通）

1. 创建工程导入坐标

   ```
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
   </dependency>
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
   </dependency>
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   ```

2. 引导启动类添加开启注解，配置文件指定服务端口

   ```
   @EnableHystrixDashboard
   ```

3. 被监控的服务要依赖actuator，并开启断路器@EnableCircuitBreaker

   ```
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   ```

4. 暴露 Actuator 的所有端点的

   ```
   management:
     endpoints:
       web:
         exposure:
           include: '*'
     endpoint:
         health:
           show-details: ALWAYS
   ```

5. 启动服务，此时可以访问到主页

   地址：http://localhost:10001/hystrix  // 10001是自己定义的端口

6. 表盘数据

   1. 一列有色数据是对应右边表头同种颜色数据
   2. 圈代表目前调用频次，越大越多，颜色绿、黄、橙、红。线则记录了时间线调用频率，

##7. 路由网关Zuul

​	将外部请求转发到微服务实例上，实现外部访问统一接口。注册入微服务，然后获取其他微服务信息，管理。

 1. 导入坐标

    ```
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    ```

	2. 启动类开启网关

    ```
    @EnableZuulProxy
    ```

	3. 配置服务端口，注册服务，和路由修改

    ```
    server:
      port: 10002
    
    spring:
      application:
        name: zuul10002
    
    eureka:
      client:
        service-url:
          defaultZone: http://localhost:17001/eureka/   # 单机交互地址集群交互地址
      instance:
          prefer-ip-address: true                       # 配置status那块的地址信息显示IP
          instance-id: zuul10002                    # 配置服务status那块的名字
    
    info:
      app.name: provider project
      company.name: www.tiantian.com
      build.artifactId: $project.artifactId$
      build.version: $project.version$
    
    zuul:
      prefix: /tiantian                          # 微服务地址的前缀
      routes:
        mydept.serviceId: feignprovider18081    # 真微服务名字
        mydept.path: /tianzuul/**               # 代理地址，用此替换微服务名字
      ignored-services: feignprovider18081      # 禁用真实微服务地址
      # ignored-services: "*"   禁用所有微服务名
    
    # 微服务地址 http://localhost:10002/feignprovider18081/feignProvider/id/23
    # 代理地址：http://localhost:10002/tianzuul/feignProvider/id/29
    # 加前缀后：http://localhost:10002/tiantian/tianzuul/feignProvider/id/28
    # 微服务地址被禁用，代理地址加了前缀，最终访问地址是第三个
    ```

	4. 测试访问

    http://网关域名:端口/微服务名/访问路径      // 注意是Provider

    ```
    http://localhost:10002/tiantian/tianzuul/feignProvider/id/28
    ```

## 8. SpringCloudConfig

 为微服务架构提供集中化的外部配置支持，配置服务器为各个不同的微服务应用的所有环境提供了中心化的外部配置。配置发生变化时服务不需要重启即可感知到配置变化并应用。

1. 创建工程导入依赖

   ```
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-config-server</artifactId>
   </dependency>
   ```

2. 创建上传git配置文件

   ```
   spring:
     profiles:
       active:
       - dev
   ---
   spring:
     profiles: dev
     application:
       name: tian-dev
   ---
   spring:
     profiles: test
     application:
       name: tian-test
   ```

   

3. 引导启动类开启注解

   ```
   @EnableConfigServer
   ```

4. 配置端口号，服务名，git等信息

   ```
   server:
     port: 10003
   spring:
     application:
       name: tiantianconfig
     cloud:
       config:
         server:
           git:
             uri: https://github.com/pwaydn/cloud-config.git
             search-paths: /**         # 路径
             default-label: master     # 分支
             username: your account
             password: your password
   ```

4. 启动，访问

   第一种方式：http://localhost:10003/application-dev.yml

   第二种方式：http://localhost:10003/master/application-dev.yml

   第二种方式：http://localhost:10003/application/test/master

   前两种的格式一样，第三种是Json格式

   
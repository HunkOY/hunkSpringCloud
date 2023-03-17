##SpringCloud
####1，单体架构，SOA架构，微服务架构
####2，什么是微服务
- 微服务一词来自Martin Fowler（马丁福勒）的Microservices一文
- 微服务是一种架构风格，将单体应用划分为小型的服务单元
- 微服务之间采用HTTP的API进行资源访问和操作，httpClient
####3，SpringCloud
一个工具箱，基于SpringBoot，将Netflix的多个框架进行二次封装并且通过自动化配置的方式将这些框架绑定到Spring的环境中，从而简化对这些框架的使用。
####4，SpringCloud，我们重点学习什么？
Netflix
- Eureka（尤里卡）：基于REST服务的分布式中间件，主要用于服务的注册和发现
- Ribbon（美 [ˈrɪbən]）：负载均衡框架，在微服务集群中为各个客户端的通信提供支持，主要实现中间层应用程序的负载均衡
- Feign（ 美 [fen]）：一个REST客户端，简化WebService客户端的开发
- Zuul：为微服务集群提供代理，过滤，路由等功能
- Hystrix（ 美 [hɪst'rɪks] ）：容错框架，通过添加延迟阈值及容错的逻辑，帮助我们控制分布式系统间组件的交互

其他
- SpringCloud Config：管理集群中的配置文件，统一配置中心
- SpringCloud Sleuth：服务跟踪框架，可以跟Zipkin，Apache HTrace和ELK等数据分析，服务跟踪系统进行整合
 - SpringCloud Stream：用于构建消息驱动的微服务架构
- SpringCloud Bus：连接各种消息代理的集群消息总线。
5，Eureka
1，基于Netflix Eureka做了二次封装
2，包含两个组件
- Eureka Server 注册中心（相当于zookeeper）
- Eureka Client 服务的注册（相当于之前的生产者和消费者）
3，Eureka Server注册中心(eureka)
作用：提供了服务的注册和发现
创建步骤：
1，创建SpringBoot项目，添加Eureka Server

<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
2，启动类，添加注解@EnableEurekaServer，用于当前是一个Eureka服务
3，yml配置注册中心的地址

server:port: 8761
4，关闭自我注册
问题：
com.netflix.discovery.shared.transport.TransportException: Cannot execute request on any known server
原因：
服务器启动时，会把自己当做一个客户端，去注册Eureka服务器，并且会到Eureka服务器抓取注册信息，我们可以通过配置改变这种行为

eureka:
 client:
  register-with-eureka: false
  fetch-registry: false
5，访问，查看服务器控制台

 
4，Eureka Client(client)---编写服务提供者-生产者
作用：注册服务，提供服务
操作步骤：
1，创建SpringBoot项目，添加Eureka Discovery

<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
2，启动类，添加注解@EnableEurekaClient
3，yml配置注册中心的地址

server:
port: 8080
eureka:
client:
service-url:
defaultZone: http://localhost:8761/eureka/
spring:
application:
name: eureka_provider
4，启动服务，查看注册中心控制台



5，开发Controller，提供对外的服务接口
6，开发多个Eureka Client，构成负载均衡
快速复制一份，修改端口，-Dserver.port=8081



5，Eureka Client(client)---编写调用者（具体如何调用后续再讲）
作用：服务的调用者，也同样需要注册到Eureka服务器，来调用其他客户端发布的服务。
操作步骤：
1，创建SpringBoot项目，添加Eureka Discover
2，启动类，添加注解@EnableEurekaClient
3，配置application.yml

server:
port: 9090
eureka:
client:
service-url:
defaultZone: http://localhost:8761/eureka/
spring:
application:
name: eureka_consumer
6，目前架构图如下：



7，Eureka注册中心的高可用
创建两个Eureka，并互相注册
每个Eureka注册中心，需要将自己当做客户端来看，彼此注册到对方上

eureka:
client:
service-url:
defaultZone: http://localhost:8761/eureka/
server:
port: 8762
注意，启动第一个注册中心会报错，等第二个注册中心启动后，会自动恢复正常
修改我们Eureka各个客户端，注册中心的地址需要修改为多个，用“，”隔开

eureka:
client:
service-url:
defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/
8，最终的Eureka架构图
不管是客户端还是服务端，都可以复制多个实例，方便扩展



9，Eureka的常用配置
心跳检测配置（客户端告诉服务端，我还活着）
客户端的实例会向服务器发送周期性的心跳，默认是30秒/次

eureka:
client:
service-url:
defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/
instance:
lease-renewal-interval-in-seconds: 30
服务器接收心跳请求，如果在一定期限内没有接收到服务实例的心跳，那么会将该实例从注册表中删除掉，这个时间是90秒
如果需要修改这个时间，可以在客户端进行设置：

eureka:
client:
service-url:
defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/
instance:
lease-renewal-interval-in-seconds: 30
lease-expiration-duration-in-seconds: 90
如果开启了自我保护机制，则实例不会被删除
客户端有可能会访问到一些无法使用的服务实例，这种情况需要使用后续的容错机制来解决。
注册表抓取间隔
客户端每30秒会去服务器端抓取注册表（可用的服务列表），并且将服务器的注册表保存到本地缓存中
修改：

eureka:
client:
service-url:
defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/
registry-fetch-interval-seconds: 30
6，Eureka和zookeeper的区别（CAP原则）
什么是CAP原则
任何一个分布式存储系统都遵循CAP原则，
即C(强一致性)、A(高可用性)、P(分区容错性)。
但是这三个原则不能同时满足，最多只能满足其中任意两个。
P（Partition Tolerance分区容错性）
以实际效果而言，分区相当于对通信的时限要求。
系统如果不能在时限内达成数据一致性，就意味着发生了分区的情况，必须就当前操作在C和A之间做出选择
就是集群中，出现部分集群可以通信，部分集群无法通信，导致某个时间窗口的数据不一致
C（Consistency 一致性）
在分布式系统中的所有数据备份，在同一时刻是否同样的值。
（等同于所有节点访问同一份最新的数据副本），换句话就是说，任何时刻，所用的应用程序都能访问得到相同的数据。
A（Availability 可用性）
在集群中一部分节点故障后，集群整体是否还能响应客户端的读写请求。
（对数据更新具备高可用性），换句话就是说，任何时候，任何应用程序都可以读写数据。
通常来说因为网络丢包不可避免，所以P是必须选择的。因此分布式存储系统往往会再A和C之间做选择
结论：zookeeper是CP设计，Eureka是AP设计
ZK有一个Leader，而且在Leader无法使用的时候通过Paxos（ZAB）算法选举出一个新的Leader。这个Leader的目的就是保证写信息的时候只向这个Leader写入，Leader会同步信息到Followers。这个过程就可以保证数据的一致性。(强一致性)
对比下ZK，Eureka不用选举一个Leader。每个Eureka服务器单独保存服务注册地址，Eureka也没有Leader的概念。这也因此产生了无法保证每个Eureka服务器都保存一直数据的特性，（最终一致性）
7，Ribbon，实现负载均衡（美 [ˈrɪbən]）
1，一个负载均衡服务器，需要包含两部分信息
1，要维护每个服务器的IP和端口信息
2，根据特定逻辑选择某个服务器
2，内部包含3个部分
Rule：一个逻辑组件，决定从服务器列表返回哪个服务器实例
Ping：该组件使用定时器来确保服务器网络可以连接
ServerList：服务器列表
3，使用
1，为上述的provider工程，编写controller，提供服务

@RestController
@RequestMapping("provider")
public class ProviderController {
@RequestMapping("service")
public String service(){
System.out.println("eureka_provider service...");
return "hello,this info from the eureka_provider";
}
}
2，为上述的consumer工程，编程controller，内部调用provider开放的接口
引入依赖包：

<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
具体调用的方案：
1，在启动类，注册bean（RestTemplate）

@Bean
@LoadBalanced
public RestTemplate getRestTemplate(){
return new RestTemplate();
}
2，在controller中，注入RestTemplate

@RestController
@RequestMapping("consumer")
public class ConsumerProvider {
@Autowired
private RestTemplate restTemplate;
@RequestMapping("service")
public String service(){
System.out.println("consumer service....");
ResponseEntity<String> forEntity =
restTemplate.getForEntity("http://eureka_provider/provider/service", String.class);
return forEntity.getBody();
}
}
4，@LoadBalanced原理说明
RestTemplate本身是Spring-web项目的一个REST客户端，提供了我们调用HTTP服务的能力
RestTemplate本身不具备负载均衡的能力，也跟SpringCloud没有关系
加入@LoadBalanced注解后，Spring容器在启动时，会为这些修饰了注解的RestTemplate添加拦截器
拦截器使用loadBalancerClient来处理请求，loadBalancerClient就是spring封装的负载均衡客户端
通过这样的间接处理，就让RestTemplate具备了负载均衡的能力
血的教训
访问消费者接口，消费者内部访问提供者，会报错
There was an unexpected error (type=Internal Server Error, status=500).
Request URI does not contain a valid hostname: http://eureka_provider/provider/service
原因是服务名称不能用“_”，可以用“-”
面试题：
Ribbon负载均衡和Nginx的负载均衡有什么不同？
Nginx是服务器端负载均衡，负载均衡的策略算法是在服务器端实现的。
Ribbon是客户端负载均衡，负载均衡算法是由调用者本身维护的



8，Feign，REST客户端（美 [fen] ）
1，什么是Feign
Feign目的是简化对WebService客户端的开发。其作用类似于我们之前学过的CXF。
Feign可以使用注解来修饰接口，被修饰的接口将具备访问webService的能力
Feign跟Eureka，Ribbon集成后，就具备了负载均衡的能力
2，如何使用
1，引入坐标：

<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
2，启动类，添加注解：@EnableFeignClients
3，在消费者端，自定义接口

@FeignClient(value = "eureka-provider")
public interface IProviderService {
@RequestMapping("provider/service")
public String service();
}
4，controller注入service，实现服务的调用
9，断路器，Hystrix--SpringCloud的保护机制，也称熔断机制（美 [hɪst'rɪks] ）
1，背景
在微服务架构中，根据业务来拆分成一个个的服务，服务与服务之间可以相互调用。
为了保证其高可用，单个服务通常会集群部署。
由于网络原因或者自身的原因，服务并不能保证100%可用，如果单个服务出现问题，调用这个服务就会出现线程阻塞，
此时若有大量的请求涌入，Servlet容器的线程资源会被消耗完毕，导致服务瘫痪。
服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的“雪崩”效应。
为了解决这个问题，业界提出了断路器模型。
2，什么是Hystrix
Hystrix是Netflix下的一个Java库，通过添加延迟阈值以及容错的逻辑，来帮助我们控制分布式系统间组件的交互。
Hystrix通过隔离服务间的访问点，停止他们之间的级联故障，提供可回退操作来实现容错
条件：
当对特定的服务的调用的不可用达到一个阀值（Hystric 是5秒20次）
断路器将会被打开



3，什么是服务熔断？
熔断就跟保险丝一样，当一个服务请求并发特别大，服务器已经招架不住了，调用错误率飙升，当错误率达到一定阈值后，就将这个服务熔断了。
熔断之后，后续的请求就不会再请求服务器了，以减缓服务器的压力。
4，什么是服务降级？
在高并发的场景下，取消一些非核心业务，将更多资源留给核心业务，从而保证核心业务正常运行
评论功能
5，SpringCloud中如何使用Hystrix
1，为消费者，引入依赖

<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
2，为启动类，添加注解：@EnableCircuitBreaker
3，在消费者的controller，加上熔断处理方法

@HystrixCommand(fallbackMethod = "serviceFallback")
@RequestMapping("service")
public String service(){
System.out.println("consumer service....!!!!");

return providerService.service();
}
/**
* 回退方法
* @return
*/
public String serviceFallback(){
System.out.println("启动熔断机制....");
return "快速返回信息";
}
10，集群网关，Zuul
1，什么是Zuul
Zuul为我们的微服务接口，提供了一个统一的网关，将集群的服务都隐藏在网关后面。
这样，客户端无需关注集群的内部结构，只需要关注网关的配置信息
SpringCloud集群也无需暴露过多的服务，提高了安全性
2，Zuul都提供了哪些功能
代理，过滤，路由
传统的代理层技术有：Nginx，Apache
zuul默认和Ribbon结合实现了负载均衡的功能。
3，Zuul的路由功能
1，创建路由工程：springcloud_zuul
pom.xml

<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>s
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>
2，applicatiom.yml

server:
port: 10000
eureka:
client:
service-url:
defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/
spring:
application:
name: zuul10000
zuul:
routes:
provider:
path: /provider/**
serviceId: eureka-provider
3，启动类

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableZuulProxy
4，启动路由工程，
访问http://localhost:10000/provider/provider/service
4，Zuul的服务过滤
1，自定义ZuulFilter

@Component
public class MyZuulFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }
    @Override
    public int filterOrder() {
        return 0;
    }
    @Override
    public boolean shouldFilter() {
        return true;
    }
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        System.out.println("--->拦截请求：" + request.getRequestURI());
        String token = request.getParameter("token");
        if(token == null){
            ctx.setSendZuulResponse(false);
            try {
                ctx.getResponse().getWriter().print("Token is null!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
2，重写方法
filterType
返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型
pre：路由之前
routing：路由之时
post： 路由之后
error：发送错误调用
filterOrder
过滤的顺序
shouldFilter
是否要过滤，本文true,永远过滤
run
过滤器的具体逻辑。可用很复杂，比如判断当前用户是否有合法权限，没有，则不放行
我们假设以请求中的token为例
11，SpringCloud Config，统一配置中心
1，什么是SpringCloud Config?
在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。
在Spring Cloud中，有分布式配置中心组件spring cloud config ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程Git仓库中。
在spring cloud config 组件中，分两个角色，一是config server，二是config client
2，Springcloud Config使用步骤
配置SpringCloud Config服务端
准备环境
在GitHub上准备一个存放配置文件的仓库



上传一个测试的配置文件application.yml
分布式配置管理中心：config_server_10086
pom.xml

<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-config-server</artifactId>
</dependency>
application.yml

server:
port: 10086
spring:
application:
name: config-server
cloud:
config:
server:
git:
uri: https://github.com/happyCoding2008/config.git
启动类
@EnableConfigServer
测试（默认要加default）
http://localhost:10086/application-default.yml
配置SpringCloud Config客户端
配置管理客户端：config_client_10088
pom.xml

<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-config</artifactId>
</dependency>
bootstrap.yml
引导上下文会在主应用上下文前创建，是主应用上下文的父上下文
引导上下文在创建时会去读取远程配置（GitHub）

server:
port: 10088
spring:
cloud:
name: application #github上配置文件的名称，没有后缀
label: master #分支
uri: http://localhost:10086 #配置中心地址
profile: default
自定义Controller

@RestController
@RequestMapping("client")
public class ClientController {
@Value("${server.name}")
private String name;
@RequestMapping("/config")
public String readConfig(){
System.out.println("读取的配置:" + name);
return name;
}
}
测试
异常记录
Caused by: org.yaml.snakeyaml.error.YAMLException: java.nio.charset.MalformedInputException: Input length = 1
编码不一致，要将工作空间的编码设置为utf-8(setting)
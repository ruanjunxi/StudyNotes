## Spring

#### IOC（Inversion of control）

IoC即控制反转，它不是种技术而是一种思想。

- 传统的开发思想：是在A类中手动new出一个B的对象；
- 使用IoC思想：不通过new来创建对象，而是通过IoC容器来帮助我们实例化对象。
  - **控制** ：指的是对象创建（实例化、管理）的权力
  - **反转** ：控制权交给外部环境（IoC 容器）

**解决的问题：**

例如：现有一个针对 User 的操作，利用 Service 和 Dao 两层结构进行开发。传统的思想是：在service实现类中new一个dao的具体实现类而不是dao接口。如果后续新需求需要针对IUserDao接口开发出一个新的实现类，那么需要手动去所有使用到dao实现类的地方将其改为新的实现类，这是很麻烦的。

![IOC之前](.\ref\IOC之前.png)

使用 IoC 的思想，我们将对象的控制权（创建、管理）交有 IoC 容器去管理，我们在使用的时候直接向 IoC 容器 “要” 就可以了

<img src=".\ref\IOC之后.png" alt="IOC之后"  />

IoC 最常见以及最合理的实现方式叫做**依赖注入**（Dependency Injection，简称 DI）。

#### AOP（Aspect oriented programming）

AOP将横切关注点（如日志记录、事务管理、权限控制、接口限流、接口幂等等）从 **核心业务逻辑（core concerns，核心关注点）** 中分离出来，实现关注点的分离。即把公共行为与主业务分离开来，这样就不需要在每个主业务都去配置公共的内容。

Spring AOP 就是基于动态代理实现，并同时集成了 AspectJ。

**使用场景：**

- 日志记录：自定义日志记录注解，利用 AOP，一行代码即可实现日志记录。

- 性能统计：利用 AOP 在目标方法的执行前后统计方法的执行时间，方便优化和分析。

- 事务管理：`@Transactional` 注解可以让 Spring 为我们进行事务管理比如回滚异常操作，免去了重复的事务管理逻辑。`@Transactional`注解就是基于 AOP 实现的。

- 权限控制：利用 AOP 在目标方法执行前判断用户是否具备所需要的权限，如果具备，就执行目标方法，否则就不执行。例如，SpringSecurity 利用`@PreAuthorize` 注解一行代码即可自定义权限校验。

- 接口限流：利用 AOP 在目标方法执行前通过具体的限流算法和实现对请求进行限流处理。

- 缓存管理：利用 AOP 在目标方法执行前后进行缓存的读取和更新。

### 面试题

#### @Component 和 @Bean 的区别是什么？



## Springboot

#### 为什么有springboot，它的优缺点？

Spring本身是轻量级的开发框架，但其配置确实重量级的，需要配置大量的内容，springboot旨在简化Spring开发，减少配置文件。

#### 什么是springboot starters

springboot starters就是一系列依赖关系的集合；我们只需要添加一个spring-boot-starter-web一个依赖就可以了，这个依赖包含的子依赖中包含了我们开发 REST 服务需要的所有依赖。

#### 介绍一下SpringBootApplication注解

可以把@SpringBootApplication注解看作是@Configuration、@EnableAutoConfiguration、@ComponentScan注解的集合

- ``@EnableAutoConfiguration``：启用springboot的自动配置机制
- ``@ComponentScan``：扫描被``@component``、``@Service``、``@Contoller``注解的bean，注解默认会扫描该类所在包下的所有文件；
- ``@Configuration``：允许在上下文中注册额外的bean或导入其他配置类。

#### springboot的自动配置如何实现

``@EnableAutoConfiguration``这个注解下有个方法将所有自动配置的信息以List方法返回。这些配置信息会被spring容器作bean管理。有了自动配置信息，只需要再使用``@Conditional``注解，以条件形式完成自动配置。


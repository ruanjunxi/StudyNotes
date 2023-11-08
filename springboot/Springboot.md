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

<<<<<<< HEAD
- `@Component` 注解作用于类，而`@Bean`注解作用于方法。
- `@Component` 需要开启`@ComponentScan`这个注解来定义扫描的路径，从而找出需要装配的类自动装配到IOC容器中。`@bean`则是在定义时就告诉spring，这是某个类的实例，后续需要的时候返回。
- 如果需要将第三方的类库中的类装配到IOC容器中，就需要使用`@bean`注解

#### 注入bean的注解有哪些

- `@Autowired`
- `@Resource`
- `@Inject`

#### @Autowired 和 @Resource 的区别是什么？

- `Autowired` 属于Spring内置的注解，优先根据接口类型去匹配bean并注入，如果一个接口类型有多个实现类，则会根据名称来匹配，名称通常是类名首字母小写。建议通过 `@Qualifier` 注解来显式指定名称而不是依赖变量的名称。举个例子，`SmsService` 接口有两个实现类: `SmsServiceImpl1`和 `SmsServiceImpl2`，且它们都已经被 Spring 容器所管理。

- ```java
  // 报错，byName 和 byType 都无法匹配到 bean
  @Autowired
  private SmsService smsService;
  // 正确注入 SmsServiceImpl1 对象对应的 bean
  @Autowired
  private SmsService smsServiceImpl1;
  // 正确注入  SmsServiceImpl1 对象对应的 bean
  // smsServiceImpl1 就是我们上面所说的名称
  @Autowired
  @Qualifier(value = "smsServiceImpl1")
  private SmsService smsService;
  ```

- `@Resource` 属于JDK内部的注解，优先根据名称来匹配，如果没找到再根据类型匹配。

- ```java
  // 报错，byName 和 byType 都无法匹配到 bean
  @Resource
  private SmsService smsService;
  // 正确注入 SmsServiceImpl1 对象对应的 bean
  @Resource
  private SmsService smsServiceImpl1;
  // 正确注入 SmsServiceImpl1 对象对应的 bean（比较推荐这种方式）
  @Resource(name = "smsServiceImpl1")
  private SmsService smsService;
  
  ```

####  Bean 的作用域有哪些?

Spring 中 Bean 的作用域通常有下面几种：

- **singleton（单例）** : IoC 容器中只有唯一的 bean 实例。Spring 中的 bean 默认都是单例的，是对单例设计模式的应用。
- **prototype（多例）** : 每次获取都会创建一个新的 bean 实例。也就是说，连续 `getBean()` 两次，得到的是不同的 Bean 实例。
- **request** （仅 Web 应用可用）: 每一次 HTTP 请求都会产生一个新的 bean（请求 bean），该 bean 仅在当前 HTTP request 内有效。
- **session** （仅 Web 应用可用） : 每一次来自新 session 的 HTTP 请求都会产生一个新的 bean（会话 bean），该 bean 仅在当前 HTTP session 内有效。
- **application/global-session** （仅 Web 应用可用）：每个 Web 应用在启动时创建一个 Bean（应用 Bean），该 bean 仅在当前应用启动时间内有效。
- **websocket** （仅 Web 应用可用）：每一次 WebSocket 会话产生一个新的 bean。

#### Bean 是线程安全的吗？

Bean在`prototype` 作用域下，是线程安全的。在**singleton** 单例作用域下，如果Bean中有可变的成员变量，那么会出现线程安全的问题。假设controller层中调用了service中某个方法Test()，该方法对service中的成员变量进行修改，那么在多线程情况下，因为是单例模式，所以会出现多个线程一起调用Test()方法，从而该成员变量出现并发问题。可以使用ThreadLocal类。

#### Bean 的生命周期了解么?

#### Spring AOP 和 AspectJ AOP 有什么区别？

AOP实现的两种方式：`Spring AOP` 和`AspectJ AOP`

- Spring AOP是利用动态代理的机制来实现的，如果一个Bean实现了某个接口，那么就会使用JDK动态代理来生成代理对象，否则就是用CGlib来生成当前类的代理对象。代理对象的作用就是用于代理目标对象，当代理对象在执行某个方法时，会在该方法上添加一些切面逻辑如：登陆校验，权限控制，日志记录等。
- 增加的切面逻辑称为增强。**Spring AOP **属于运行时增强，而 AspectJ 是编译时增强。**Spring AOP** 基于代理(Proxying)，而 AspectJ 基于字节码操作(Bytecode Manipulation)。

####  AspectJ 定义的通知类型有哪些？

- **Before**（前置通知）：目标对象的方法调用之前触发
- **After** （后置通知）：目标对象的方法调用之后触发
- **AfterReturning**（返回通知）：目标对象的方法调用完成，在返回结果值之后触发
- **AfterThrowing**（异常通知）：目标对象的方法运行中抛出 / 触发异常后触发。
- **Around** （环绕通知）：编程式控制目标对象的方法调用。环绕通知是所有通知类型中可操作范围最大的一种，因为它可以直接拿到目标对象，以及要执行的方法，所以环绕通知可以任意的在目标对象的方法调用前后搞事，甚至不调用目标对象的方法

#### Spring MVC的核心组件

- DispatcherServlet：核心中央处理器，负责接收请求，分发，并给予客户端响应
- HandlerMapping：处理器映射器，根据请求的URL来匹配能够处理这个请求的Handle；
- HandlerAdapter：根绝HandlerMapping匹配到的Handler，适配执行对应的Handler；
- Handler：请求处理器，执行实际请求的处理器；
- ViewResolver：视图解析器，根据Handler返回的视图，渲染成实际视图并返回。

#### 统一的异常处理怎么做

会使用到：`@ControllerAdvice`  以及`@ExcetionHandler`这两个注解。

- `@ControllerAdvice` 注解会将所有或指定的Controller编入异常处理的逻辑(AOP)中，当controller中出现异常时，就会使用被`@ExcetionHandler`修饰的方法处理；
- `ExceptionHandlerMethodResolver` 中 `getMappedMethod` 方法决定了异常具体被哪个被 `@ExceptionHandler` 注解修饰的方法处理异常。
- **`getMappedMethod()`会首先找到可以匹配处理异常的所有方法信息，然后对其进行从小到大的排序，最后取最小的那一个匹配的方法(即匹配度最高的那个)。**

#### Spring框架中用到哪些设计模式



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


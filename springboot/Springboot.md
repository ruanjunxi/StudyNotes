## Spring

#### IOC（Inversion of control）

IoC即控制反转，它不是种技术而是一种思想。

- 传统的开发思想：是在A类中手动new出一个B的对象；
- 使用IoC思想：不通过new来创建对象，而是通过IoC容器来帮助我们实例化对象。
  - **控制** ：指的是对象创建（实例化、管理）的权力
  - **反转** ：控制权交给外部环境（IoC 容器）

**解决的问题：**

- 实现fast-fail：所有的Bean会在容器启动时被创建并实例化，如果Bean之间的依赖关系存在问题，那么程序会在启动时报错，而不会等到实际运行时才报错，从而使得生产更可靠，更稳定。
- 降低耦合：在Spring中，高层只依赖于低层的抽象接口，而低层实现这个接口。我们只需要把实现类交由IOC容器，在需要的时候自动注入到接口中。

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

#### Aop实战，注解开发

##### 定义注解

```java
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String Module() default "";
    String Operator() default "";
}
```

##### 编写切面逻辑

注意：`@Around` 对应 `ProceedingJoinPoint`。

```java
@Component
@Aspect
public class LogAspect {
	//定义需要处理的注解
    @Pointcut("@annotation(com.example.aopdemo.commom.aop.LogAnnotation)")
    public void pt(){

    }
    
    @Before("pt()")
    public void logBefore(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName+"方法的参数是:"+joinPoint.getArgs().toString());
    }
	 @Around(value = "pt()")
    public void logAround(ProceedingJoinPoint joinPoint){
        
    }
    @AfterReturning(value = "pt()",returning = "result")
    public void logAfter(JoinPoint joinPoint,Object result){
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName+"方法的返回结果:"+result);
    }
}
```




### 面试题

#### @Component 和 @Bean 的区别是什么？

- `@Component` 注解作用于类，而`@Bean`注解作用于方法，该方法会返回某个类的实例。
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
- HandlerAdapter：根据HandlerMapping匹配到的Handler，适配执行对应的Handler；
- Handler：请求处理器，执行实际请求的处理器；
- ViewResolver：视图解析器，根据Handler返回的视图，渲染成实际视图并返回。

#### 统一的异常处理怎么做

会使用到：`@ControllerAdvice`  以及`@ExcetionHandler`这两个注解。

- `@ControllerAdvice` 注解会将所有或指定的Controller编入异常处理的逻辑(AOP)中，当controller中出现异常时，就会使用被`@ExcetionHandler`修饰的方法处理；
- `ExceptionHandlerMethodResolver` 中 `getMappedMethod` 方法决定了异常具体被哪个被 `@ExceptionHandler` 注解修饰的方法处理异常。
- **`getMappedMethod()`会首先找到可以匹配处理异常的所有方法信息，然后对其进行从小到大的排序，最后取最小的那一个匹配的方法(即匹配度最高的那个)。**

#### Spring框架中用到哪些设计模式

- 工厂模式 : Spring 使用工厂模式通过 `BeanFactory`、`ApplicationContext` 创建 bean 对象。
- 单例模式：Spring中的Bean默认是单例的。
- 代理模式：AOP的功能实现。
- 模板方法模式：Spring 中 `jdbcTemplate`、`hibernateTemplate` 等以 Template 结尾的对数据库操作的类，它们就使用到了模板模式。
- 观察者模式：Spring 事件驱动模型就是观察者模式很经典的一个应用。
- 适配器模式：Spring AOP 的增强或通知(Advice)使用到了适配器模式、spring MVC 中也是用到了适配器模式适配`Controller`。

#### Spring 管理事务的方式有几种？

- 编程式事务：通过 `TransactionTemplate`或者 `TransactionManager` 手动管理事务，实际应用中很少使用。
- 声明式事务：使用`@Transactional`注解。

#### @Transactional常用参数

- **readOnly**（默认为`false`）：之情事务是否为只读事务；
- **propagation**：指定事务的传播行为，具体见下一问。
- **isolation**：事务的隔离级别。
  - `DEFAULT`：使用数据库的默认隔离级别。
  - `READ_UNCOMMITTED`：允许读取未提交的数据变更。
  - `READ_COMMITTED`：只能读取已提交的数据变更。
  - `REPEATABLE_READ`：可重复读取相同的数据集。
  - `SERIALIZABLE`：完全串行化所有的事务执行。
- **timeout**：超时时间，如果在指定的时间内没有完成事务操作，则事务回滚。

#### Spring 事务中哪几种事务传播行为?

当事务方法被另一个事务方法调用时，必须指定是加入进行中的事务，还是开启一个新的事务，并在自己的事务中运行。包括以下4个值:

- **`TransactionDefinition.PROPAGATION_REQUIRED`**：默认的传播行为，如果当前存在事务则加入该事务；否则就创建一个新事务。
- **`TransactionDefinition.PROPAGATION_REQUIRES_NEW`**：会开启自己的事务，并把当前事务挂起，执行自己的事务。
- **`TransactionDefinition.PROPAGATION_NESTED`**：如果当前存在事务，则在嵌套事务中执行，否则开启一个新的事务。
- **`TransactionDefinition.PROPAGATION_MANDATORY`**：如果当前存在事务则加入该事务，否则抛出异常。



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

#### Bean的循环依赖如何解决

参考：[浅谈 Spring 如何解决 Bean 的循环依赖问题 - 掘金 (juejin.cn)](https://juejin.cn/post/7218080360403615804#heading-10)

Spring采用三级缓存解决该问题，即三个Map；

- 一级缓存（`singletonObjects`）：缓存的是**已经实例化、属性注入、初始化后**的 Bean 对象。
- 二级缓存（`earlySingletonObjects`）：缓存的是**实例化后，但未属性注入、初始化**的 Bean对象（用于提前暴露 Bean）。
- 三级缓存（`singletonFactories`）：缓存的是一个 `ObjectFactory`，主要作用是生成原始对象进行 AOP 操作后的**代理对象**（这一级缓存主要用于解决 AOP 问题，后续文章中讲解）。

1. 首先会获取 `AService` 对应的 Bean 对象。

2. 先是调用 `doGetBean()` 中的第一个 `getSingleton(beanName)` 判断是否有该 Bean 的实例，有就直接返回了。（显然这里没有）

3. 然后调用 `doGetBean()` 中的第二个 `getSingleton()` 方法来执行 `doCreateBean()` 方法。

4. 先进行实例化操作（也就是利用构造函数实例化），此时实例化后生成的是原始对象。

5. 将原始对象通过 lambda表达式 进行封装成 `ObjectFactory` 对象，通过 `addSingletonFactory` 加入三级缓存中。

6. 然后再进行属性注入，此时发现需要注入 `BService` 的 Bean，会通过 `doGetBean()` 去获取 `BService` 对应的 Bean。

7. 同样调用 `doGetBean()` 中的第一个 `getSingleton(beanName)` 判断是否有该 Bean 的实例，显然这里也是不会有 `BService` 的 Bean 的。

8. 然后只能调用 `doGetBean()` 中的第二个 `getSingleton()` 方法来执行 `doCreateBean()` 方法来创建一个 `BService` 的 Bean。

9. 同样地先进行实例化操作，生成原始对象后封装成 `ObjectFactory` 对象放入三级缓存中。

10. 然后进行属性注入，此时发现需要注入 `AService` 的 Bean，此时调用调用 `doGetBean()` 中的第一个 `getSingleton(beanName)` 查找是否有 `AService` 的 Bean。此时会触发三级缓存，也就是调用 `singletonFactories.get(beanName)`。

11. 因为三级缓存中有 `AService` 的原始对象封装的 `ObjectFactory` 对象，所以可以获取到的代理对象或原始对象，并且上移到二级缓存中，提前暴露给 `BService` 调用。

12. 所以 `BService` 可以完成属性注入，然后进行初始化后，将 Bean 放入一级缓存，这样 `AService` 也可以完成创建。

![循环依赖bean](.\ref\循环依赖bean.png)

需要注意的是：

- 实例化后的 Bean 会生成原始对象，然后经过 lambda 表达式封装为 `ObjectFactory` 对象，并且通过 `addSingletonFactory()` 方法将其放入 三级缓存（`singletonFactories`）中。但是否加入三级缓存是需要判断是否出现了循环依赖的，如果出现循环依赖才会加入三级缓存。
- 而 `getEarlyBeanReference()` 方法会根据 Bean 中是否有 AOP 操作来决定返回的是 **原始对象** 还是 **代理对象**，并且会将其上移到二级缓存中（也就是提前暴露出来让别的 Bean 使用）。
- 当bean完成了整体的初始化过程才会上移至一级缓存即单例池中。

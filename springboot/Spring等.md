# Spring

#### 1.说说对spring 的理解。

Spring是一个开源框架，包含IOC与AOP。**控制反转(IOC)：**IoC 的思想就是将原本在程序中手动创建对象的控制权，交由 Spring 框架来管理。Spring容器使用了工厂模式为我们创建了所需要的对 象，我们使用时不需要自己去创建，直接调用Spring为我们提供的对象即可，这就是控制反转的思想。**依赖注入(DI)：**Spring使用JavaBean对象的set方法或者带参数的构造方法为我们在创建所需对象时进行初始化的过程就是依赖注入。**面向切面编程(AOP)：**在面向切面编程中，我们将一个个对象某些类似的方面横向抽象成一个切面，对这个切面进行一些如权限验证，事物管理，记录日志等公用操作处理的过程就是面向切面编程的思想。

在Spring中，所有管理的都是JavaBean对象，而BeanFactory和ApplicationContext就是Spring框架 的那个IOC容器，现在一般使用ApplicationContext，其不但包括了BeanFactory的作用，同时还进行了 更多的扩展。

#### 2.spring框架有哪些优势？

- 轻量：Spring 是轻量的，基本的版本大约2MB。
- 控制反转：Spring通过控制反转实现了松散耦合，对象们给出它们的依赖，而不是创建或查找依赖的对象们。

- 面向切面的编程(AOP)：Spring支持面向切面的编程，并且把应用业务逻辑和系统服务分开。

- 容器：Spring 包含并管理应用中对象的生命周期和配置。
- 异常处理：Spring 提供方便的API把具体技术相关的异常（比如由JDBC，Hibernate or JDO抛出的） 转化为一致的unchecked 异常。

#### 3.Spring框架使用了哪些设计模式？

- **单例模式：**在Spring中，默认情况下，一个Bean只会被创建一次，这就是单例模式。也就是说，每次从容器中获取该Bean时，都会返回同一个实例对象。**关键代码：构造函数是私有的。**

- **原型模式：**这种模式是实现了一个原型接口，该接口用于创建当前对象的克隆。当直接创建对象的代价比较大时，则采用这种模式。例如，一个对象需要在一个高代价的数据库操作之后被创建。我们可以缓存该对象，在下一个请求时返回它的克隆，在需要的时候更新数据库，以此来减少数据库调用。**在Spring中**，原型模式通常被用于创建Bean对象。Spring容器会根据配置文件或注解创建对应的Bean实例，并将其缓存起来以供后续使用。当需要创建新的对象时，Spring容器会直接复制缓存中的实例，而不是重新创建一个相同的对象。

- **工厂模式：**在Spring中，工厂模式被广泛应用于创建和管理对象实例，这种方式被称为IoC容器，即依赖注入容器。它是通过将对象的创建和组装移交给Spring容器来实现的。

- **适配器模式（Adapter Pattern）：**作为两个不兼容的接口之间的桥梁。这种类型的设计模式属于结构型模式，它结合了两个独立接口的功能。

  这种模式涉及到一个单一的类，该类负责加入独立的或不兼容的接口功能。举个真实的例子，读卡器是作为内存卡和笔记本之间的适配器。您将内存卡插入读卡器，再将读卡器插入笔记本，这样就可以通过笔记本来读取内存卡。

  - Spring中适配器模式主要是用于处理请求的，其中包含两种类型的适配器：HandlerAdapter和ViewResolver。
  - HandlerAdapter是Spring MVC框架中的一个关键组件，用于将请求与Controller方法进行适配。当一个请求到达DispatcherServlet时，它会根据请求的URL找到对应的Controller方法，但请求的参数可能不符合Controller方法的参数列表。这时就需要使用HandlerAdapter来将请求适配成Controller所需要的参数形式，并且调用Controller方法。HandlerAdapter还可以处理异步请求、文件上传等特殊情况。
  - ViewResolver也是Spring MVC框架中的一个重要组件，用于将Controller方法返回的数据适配成视图。当Controller方法处理完请求后，会返回一个ModelAndView对象，该对象包含了数据和视图名称。而ViewResolver就是用于将这个ModelAndView对象适配成具体的视图。在Spring MVC中有多种视图解析器，常见的包括InternalResourceViewResolver和FreeMarkerViewResolver等。

- **装饰器模式（Decorator Pattern）：**允许向一个现有的对象添加新的功能，同时又不改变其结构。这种类型的设计模式属于结构型模式，它是作为现有的类的一个包装。
  -  在Spring框架中，包装模式最常见的应用是AOP（面向切面编程）。在这种情况下，Spring使用代理对象作为目标对象的包装器。代理对象充当了目标对象和客户端之间的中介，截获对目标对象的调用并在必要时执行额外的逻辑。 Spring支持两种类型的代理：JDK动态代理和CGLIB代理。
  - 除AOP之外，Spring还使用包装模式来创建适配器。适配器将现有类的接口转换为需要的接口。 Spring中的JDBC适配器就是一个例子。 JDBC驱动程序提供了一组特定的接口，但是Spring希望使用不同的接口与它进行交互。因此，Spring创建了一个适配器来将JDBC驱动程序的接口转换为Spring所需的接口。

- **代理模式：**在Spring中，有两种代理模式：JDK动态代理和CGLIB代理。Spring框架会根据配置自动选择合适的代理模式。如果目标对象实现了接口，则使用JDK动态代理；否则使用CGLIB代理。如果想强制使用某种代理模式，可以通过配置文件进行设置。
  - JDK动态代理主要通过Java反射机制实现，它需要目标对象实现一个接口，并将目标对象传递给代理工厂类，代理工厂类会生成一个实现了相同接口的代理对象。当调用代理对象方法时，实际上是调用了InvocationHandler接口中的invoke()方法，该方法会根据参数对目标对象进行调用。因此，JDK动态代理只能代理实现了接口的目标对象。
  - CGLIB代理则不需要目标对象实现接口，它是通过字节码技术动态生成目标对象子类的方式实现代理。CGLIB代理不能代理final修饰的方法和类。

- **观察者模式：**定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。
  - 在Spring中，当一个bean发生变化时，它可以通知应用程序上下文(ApplicationContext)。应用程序上下文然后可以将这些更改传播给其他相关的bean。这种通知机制就是基于观察者模式实现的。
  - 具体地说，在Spring中，当一个bean发生变化时，它会发送一个事件（Event）。可以通过在bean类中注入ApplicationEventPublisher来实现这一点。接着，其他相关的bean可以注册监听器（Listener），以便在事件发生时进行通知和响应。

- **策略模式：**它通常用于将某个算法或业务逻辑从主程序中分离出来，以便在不同的情况下使用不同的实现。下面是一些在Spring中使用策略模式的例子：
  - Spring Security 中的 AuthenticationProvider 接口： AuthenticationProvider 是一个接口，定义了验证用户身份的方法 authenticate()。在Spring Security中，可以有多个AuthenticationProvider实现类，每个实现类的authenticate()方法根据不同的方式进行验证。
  - Spring Boot 中的 DataSource 选择器：在Spring Boot中，可以使用多种方式配置数据源。其中一种方式是使用spring.datasource.type属性指定数据源类型。Spring Boot会自动根据指定的类型创建相应的数据源实例。
  - Spring Framework 中的 ResourceLoader 接口：ResourceLoader 接口定义了通过URL获取资源的方法。Spring Framework中提供了多种实现方式，包括ClassPathResource、FileUrlResource、ServletContextResource等。这样就可以根据不同的情况选择不同的实现。

- **模板模式：**定义一个操作中的算法的骨架，而将一些步骤延迟到子类中。模板方法使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。
  - 具体而言，模板模式定义了一个通用算法的骨架，将一些实现步骤留给子类来实现。在Spring中，这个通用算法可以是一段模板方法，将一些预定义的操作顺序和一些“挂钩”方法组合在一起，以完成一个特定的任务（例如，从数据库中读取数据并将其转换为Java对象）。
  - 在Spring中，最常见的模板模式实现是JdbcTemplate类。该类封装了许多常见的JDBC操作，并将它们组合成一个通用的算法。用户只需要重写JdbcTemplate的一些“挂钩”方法，就可以实现自己的JDBC操作，而无需编写重复的JDBC代码。
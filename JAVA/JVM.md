## Java内存区域篇

Java 虚拟机在执行 Java 程序的过程中会把它管理的内存划分成若干个不同的数据区域。

线程私有的：1.程序计数器，2.本地方法栈，3.虚拟机栈

线程共享的：1.堆，2.方法区，3.直接内存

![内存区域](D:\rjx\Doc\JAVA\ref\进程与线程.png)

#### 程序计数器

程序计数器是一块较小的内存空间，可以看作是当前线程所执行的字节码的行号指示器。字节码解释器工作时通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等功能都需要依赖这个计数器来完成。

#### Java虚拟机栈

Java中方法调用的数据需要通过栈进行传递，每一次方法调用都会有一个对应的栈帧被压入栈中，每一个方法调用结束后，都会有一个栈帧被弹出。

TODO

#### 本地方法栈

和虚拟机栈所发挥的作用非常相似，区别是：**虚拟机栈为虚拟机执行 Java 方法 （也就是字节码）服务，而本地方法栈则为虚拟机使用到的 Native 方法服务。** 在 HotSpot 虚拟机中和 Java 虚拟机栈合二为一。

#### 堆

Java虚拟机所管理的内存最大的一块，此内存区域的唯一目的就是存放对象实例。Java堆是垃圾收集器管理的主要区域，也被称为GC堆。

#### 方法区

当虚拟机需要使用一个类时，他需要读取并解析Class文件，再将信息存入方法区。方法区会存储已被虚拟机加载的 **类信息、字段信息、方法信息、常量、静态变量、即时编译器编译后的代码缓存等数据**。

**方法区与元空间、永久代的关系：** 方法区和永久代以及元空间的关系很像 Java 中接口和类的关系，类实现了接口，这里的类就可以看作是永久代和元空间，接口可以看作是方法区，也就是说永久代以及元空间是 HotSpot 虚拟机对虚拟机规范中方法区的两种实现方式。并且，永久代是 JDK 1.8 之前的方法区实现，JDK 1.8 及以后方法区的实现变成了元空间。

#### 运行时常量池

#### 字符串常量池

**字符串常量池** 是 JVM 为了提升性能和减少内存消耗针对字符串（String 类）专门开辟的一块区域，主要目的是为了避免字符串的重复创建。低层是实现一个StringTable，可以理解为一个hashTable，key就是字符串，value就是其地址。

#### 直接内存

直接内存是通过`java.nio.ByteBuffer` 类来分配的一种特殊的内存，其是在堆外分配的。

在**NIO（Non-Blocking I/O，也被称为 New I/O）**中，引入了一种基于通道与缓存区的I/O方式，他可以使用native函数库在堆外分配一段内存，并使用**DirectByteBuffer 对象作为这块内存的引用进行操作**，从而在一些场景中显著提升性能，避免了在Java堆和native堆之间来回复制数据。

### HotSpot虚拟机

## java垃圾回收篇

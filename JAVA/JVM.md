## Java内存区域篇

Java 虚拟机在执行 Java 程序的过程中会把它管理的内存划分成若干个不同的数据区域。

线程私有的：1.程序计数器，2.本地方法栈，3.虚拟机栈

线程共享的：1.堆，2.方法区，3.直接内存

![内存区域](D:\rjx\Doc\JAVA\ref\进程与线程.png)

#### 程序计数器

程序计数器是一块较小的内存空间，可以看作是当前线程所执行的字节码的行号指示器。字节码解释器工作时通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等功能都需要依赖这个计数器来完成。

#### Java虚拟机栈

Java中方法调用的数据需要通过栈进行传递，每一次方法调用都会有一个对应的栈帧被压入栈中，每一个方法调用结束后，都会有一个栈帧被弹出。

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

#### HotSpot虚拟机

## Java垃圾回收篇

堆内存被分为：1.新生代内存(Young Generation)，2.老生代(Old Generation)，3.元空间(**Metaspace**)

![垃圾回收堆](D:\rjx\Doc\JAVA\ref\垃圾回收堆.png)

#### 内存分配和回收原则

对象会优先在Eden区里分配，如果Eden区空间不足，则会进行一次`Minor GC`。没经过一次`Minor GC` 对象的年龄就加1。当年龄增加到一定程度（累计的某个年龄超过`survivor `区 50%）则会进入老年代。**大对象**会直接进入老年代

针对 HotSpot VM 的实现，它里面的 GC 其实准确分类只有两大种：

- 部分收集 (Partial GC)：

  - 新生代收集（Minor GC / Young GC）：只对新生代进行垃圾收集；

  - 老年代收集（Major GC / Old GC）：只对老年代进行垃圾收集。需要注意的是 Major GC 在有的语境中也用于指代整堆收集；

  - 混合收集（Mixed GC）：对整个新生代和部分老年代进行垃圾收集。

- 整堆收集 (Full GC)：收集整个 Java 堆和方法区。

##### 空间分配担保

空间分配担保是为了确保在 Minor GC 之前老年代本身还有容纳新生代所有对象的剩余空间。

#### 死亡对象判断方法

- **引用计数法：**每当有一个引用指向该对象，其计数器加1，引用失效则减1，当计数器为0，则代表该对象死亡。**存在的问题：**如果存在循环引用，即A对象和B对象互相引用，导致他们计数器不可能为0，于是GC无法回收这两个对象。
- **可达性分析算法：**以 “**GC Root**” 的对象为起点，从这些节点向下搜索，节点所走过的路径称为引用链，不在引用链上的对象则会被标记为不可达，可以回收。

- **哪些对象可以作为 GC Roots 呢？**

  - 虚拟机栈(栈帧中的局部变量表)中引用的对象

  - 本地方法栈(Native 方法)中引用的对象

  - 方法区中类静态属性引用的对象

  - 方法区中常量引用的对象

  - 所有被同步锁持有的对象

  - JNI（Java Native Interface）引用的对象

#####  如何判断废弃常量？

假如在字符串常量池中存在字符串'ABC'，如果没有任何String对象引用该字符串常量的话，那么就认为该字符串为废弃常量。在必要时会将其清理。

##### 如何判断一个类是无用的类？

方法区主要回收无用的类，满足以下三个条件的类会被认为是无用的类：

- 该类所有的对象都已经被回收，即Java堆中不存在任何该类的对象；
- 加载该类的`ClassLoader` 也被回收；
- 该类对应的 `java.lang.Class` 对象没有在任何地方被引用，无法在任何地方通过反射访问该类的方法。

#### 垃圾收集算法

##### 标记-清除算法

标记所有可达的对象，清除所有没被标记的对象。会出现两个问题：1.效率不高，2.清除后产生大量的内存碎片。

##### 复制算法

把整个内存分为大小相同的两块，垃圾收集时，将所有还存活的对象复制到另一个空的内存区域上，然后清空当前区域里的所有对象。这样既提高了效率还解决了内存碎片的问题。**存在的问题：**1. 可用内存仅为原来的一半。2.不适合老年代，因为大对象的复制十分耗时。

##### 标记-整理算法

针对老年代的特点提出的算法，在标记结束后，将存活的对象向一段移动，然后直接清理端边界以外的内存。由于加了整理过程，其效率也不高，比较适合老年代这种回收频率低的场景。

##### 分代收集算法

对于**新生代区域**，可以使用标记复制算法，只需要付出少量复制对象的成本就完成了垃圾回收。对于**老年代**，其对象的存活几率更高，则使用标记-整理算法。以上思想就体现了**HotSpot 为什么要分为新生代和老年代？**

#### 垃圾收集器

#####  Serial 收集器

串行收集器，启动GC线程进行垃圾清理时，会暂停其他所有线程，直到其清理结束。

![serial收集器](D:\rjx\Doc\JAVA\ref\serial收集器.png)

##### ParNew 收集器

ParNew 收集器其实就是 Serial 收集器的多线程版本，除了使用多线程进行垃圾收集外，其余行为（控制参数、收集算法、回收策略等等）和 Serial 收集器完全一样。**新生代采用标记-复制算法，老年代采用标记-整理算法。**

![parNew收集器](D:\rjx\Doc\JAVA\ref\parNew收集器.png)

##### Parallel Scavenge 收集器

**这是JDK1.8默认的收集器**。Parallel Scavenge 收集器关注点是吞吐量（高效率的利用 CPU），因此在新生代和老年代均使用多线程来进行垃圾回收。在注重吞吐量以及 CPU 资源的场合，都可以优先考虑 。

![Parallel Scavenge 收集器](D:\rjx\Doc\JAVA\ref\Parallel Scavenge 收集器.png)

##### CMS 收集器

**CMS（Concurrent Mark Sweep）：**目的是让停顿时间（`stop the world`）最短。即其垃圾回收器GC线程和用户线程是并发执行的。分为四个步骤：

- **初始标记：**暂停所有线程进行初始标记，记录下直接与root相连的对象，这个过程速度很快。
- **并发标记：**用户线程和并发标记线程并发运行，此过程记录所有可达对象，因为该过程用户线程还会更新引用域，所以还需记录该过程发生的变化。
- **重新标记**：重新标记在并发标记过程中用户线程更新导致所有可达对象发生变化的对象

- **并发清理：**开启用户线程，GC线程并发清理未标记的对象。

![CMS收集器](D:\rjx\Doc\JAVA\ref\CMS收集器.png)

主要优点：**并发收集、低停顿**。缺点：**回收算法采用"标记-清除"算法，会产生大量内存碎片**。

##### G1收集器

**G1 (Garbage-First) 是一款面向服务器的垃圾收集器,主要针对配备多颗处理器及大容量内存的机器. 以极高概率满足 GC 停顿时间要求的同时,还具备高吞吐量性能特征.** 他是JDK1.9默认的垃圾回收器。分为以下步骤：

- **初始标记**
- **并发标记**
- **最终标记**
- **筛选回收**

![G1收集器](D:\rjx\Doc\JAVA\ref\G1收集器.png)

**G1 收集器在后台维护了一个优先列表，每次根据允许的收集时间，优先选择回收价值最大的 Region(这也就是它的名字 Garbage-First 的由来)** 。这种使用 Region 划分内存空间以及有优先级的区域回收方式，保证了 G1 收集器在有限时间内可以尽可能高的收集效率（把内存化整为零）。

## 类加载器


## 多线程篇

### 进程与线程

**一个 Java 程序的运行是 main 线程和多个其他线程同时运行**。**现在的Java线程本质就是操作系统的线程。**	

进程：进程是程序的一次动态执行过程，是操作系统资源分配的基本单位。

线程：线程是比进程更小的执行单位，是处理机分配的基本单位。一个进程包含多个线程，一个进程的多个线程共享进程的堆和方法区资源，每个线程又拥有自己的**本地方法栈、虚拟机栈、程序计数器**。

**程序计数器：**私有主要是为了**线程切换后能恢复到正确的执行位置**

**虚拟机栈：** 每个 Java 方法在执行之前会创建一个栈帧用于存储局部变量表、操作数栈、常量池引用等信息。从方法调用直至执行完成的过程，就对应着一个栈帧在 Java 虚拟机栈中入栈和出栈的过程。

**本地方法栈：** 和虚拟机栈所发挥的作用非常相似，区别是：**虚拟机栈为虚拟机执行 Java 方法 （也就是字节码）服务，而本地方法栈则为虚拟机使用到的 Native 方法服务。** 在 HotSpot 虚拟机中和 Java 虚拟机栈合二为一。

![](D:\rjx\Doc\JAVA\ref\进程与线程.png)





### sleep()和wait()的区别

共同点：都可以暂停线程的执行

不同点：

- **sleep()不会释放锁，而 wait()会释放锁；**
- wait()调用后，不会自动苏醒，需要等待其他线程调用notify()/notifyall()来唤醒线程，也可以使用 `wait(long timeout)` 超时后线程会自动苏醒。

- `wait()` 通常被用于线程间交互/通信，`sleep()`通常被用于暂停执行。

- `sleep()` 是 `Thread` 类的静态本地方法，`wait()` 则是 `Object` 类的本地方法。

### 为什么 wait() 方法不定义在 Thread 中？

wait()会释放当前线程所占用的对象锁，每个对象(Object)都有自己的对象锁，既然要让当前线程释放所占用的对象锁，那自然是操作对应的对象来释放这个锁。同样，调用notifyall()也能唤醒其他正在等待这个对象锁的线程。

sleep()是让当前线程暂停执行，并不涉及锁，因此是定义在线程类中。

### 为什么不直接调用Thread类的run方法？

一般来说，我们会调用Thread类的start()方法，然后start()方法来调用run()方法，这样才实现了多线程工作。如果直接调用run()方法，会把run()方法当做main方法下的一个普通方法来执行，这就不是多线程工作

## 锁篇

### volatile 关键字

在 Java 中，`volatile` 关键字可以保证变量的可见性，如果我们将变量声明为 **`volatile`** ，这就指示 JVM，这个变量是共享且不稳定的，每次使用它都到主存中进行读取。`volatile` 可以保证数据的可见性但不能保证数据的原子性。volatile还可以防止指令重排。

**双重校验锁实现对象单例（线程安全）**：

```java
public class Singleton {
    private volatile static Singleton uniqueInstance;

    private Singleton() {
    }

    public  static Singleton getUniqueInstance() {
       //先判断对象是否已经实例过，没有实例化过才进入加锁代码
        if (uniqueInstance == null) {
            //类对象加锁
            synchronized (Singleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Singleton();
                }
            }
        }
        return uniqueInstance;
    }
}
```
#### 乐观锁和悲观锁

### synchronized 关键字

在 Java 早期版本中，`synchronized` 属于 **重量级锁**，效率低下。这是因为监视器锁（monitor）是依赖于底层的操作系统的 `Mutex Lock` 来实现的，Java 的线程是映射到操作系统的原生线程之上的。如果要挂起或者唤醒一个线程，都需要操作系统帮忙完成，而操作系统实现线程之间的切换时需要从用户态转换到内核态，这个状态之间的转换需要相对比较长的时间，时间成本相对较高。

不过，在 Java 6 之后， `synchronized` 引入了大量的优化如自旋锁、适应性自旋锁、锁消除、锁粗化、偏向锁、轻量级锁等技术来减少锁操作的开销，这些优化让 `synchronized` 锁的效率提升了很多。因此， `synchronized` 还是可以在实际项目中使用的，像 JDK 源码、很多开源框架都大量使用了 `synchronized` 。

`synchronized` 关键字的使用方式主要有下面 3 种：

1. 修饰实例方法：给当前对象实例加锁，进入同步代码前要获得 **当前对象实例的锁** 。

    synchronized void method() {
       //业务代码
   }

2.  修饰静态方法：给当前类加锁，会作用于类的所有对象实例 ，进入同步代码前要获得 **当前 class 的锁**

   synchronized static void method() {
       //业务代码
   }

3. 修饰代码块：对括号里指定的对象/类加锁：

   1. `synchronized(object)` 表示进入同步代码库前要获得 **给定对象的锁**。
   2. `synchronized(类.class)` 表示进入同步代码前要获得 **给定 Class 的锁**

```java
synchronized(this) {
//业务代码
}
```
#### 构造方法可以用synchronized修饰吗？

不能，因为构造方法本身就是线程安全的。

#### synchronized 和 volatile 有什么区别？

他们俩应该算是互补的存在。

- `volatile` 关键字是线程同步的轻量级实现，所以 `volatile`性能肯定比`synchronized`关键字要好 。但是 `volatile` 关键字只能用于变量而 `synchronized` 关键字可以修饰方法以及代码块 。
- `volatile`只能保证数据的可见性，不能保证原子性，因此还是需要`synchronized`的介入来保证线程安全。

### ReentrantLock

`ReentrantLock` 实现了 `Lock` 接口，是一个可重入且独占式的锁，和 `synchronized` 关键字类似。不过，`ReentrantLock` 更灵活、更强大，增加了轮询、超时、中断、公平锁和非公平锁等高级功能。

`ReentrantLock` 里面有一个内部类 `Sync`，`Sync` 继承 AQS（`AbstractQueuedSynchronizer`），添加锁和释放锁的大部分操作实际上都是在 `Sync` 中实现的。`Sync` 有公平锁 `FairSync` 和非公平锁 `NonfairSync` 两个子类。

## 线程池篇


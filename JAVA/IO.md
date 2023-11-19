### IO

#### Java IO中的设计模式有哪些

##### 装饰器模式

**装饰器模式**主要是在不改变原有对象的情况下扩展其功能。装饰器模式通过组合来替代继承从而扩展原始类的功能。

对于字节流来说， `FilterInputStream` （对应输入流）和`FilterOutputStream`（对应输出流）是装饰器模式的核心，分别用于增强 `InputStream` 和`OutputStream`子类对象的功能。

我们常见的`BufferedInputStream`(字节缓冲输入流)、`DataInputStream` 等等都是`FilterInputStream` 的子类，`BufferedOutputStream`（字节缓冲输出流）、`DataOutputStream`等等都是`FilterOutputStream`的子类。

举个例子，我们可以通过 `BufferedInputStream`（字节缓冲输入流）来增强 `FileInputStream` 的功能。

```java
public BufferedInputStream(InputStream in) {
    this(in, DEFAULT_BUFFER_SIZE);
}
// 可以看出 BufferedInputStream 构造函数有个参数就是InputStream
public BufferedInputStream(InputStream in, int size) {
    super(in);
    if (size <= 0) {
        throw new IllegalArgumentException("Buffer size <= 0");
    }
    buf = new byte[size];
}

//ZipInputStream 构造函数有个参数就是 BufferedInputStream，
// 从而展示出装饰器模式另一个重要特征，对原始类嵌套多个装饰器	
public class InflaterInputStream extends FilterInputStream {
}

public class DeflaterOutputStream extends FilterOutputStream {
}

```

另一个想法是：我们可能可以创建**`BufferedFileInputStream`**去继承`InputStream`，这样做的话会导致子类太多，继承关系很复杂。

##### 适配器模式

**适配器（Adapter Pattern）模式** 主要用于接口互不兼容的类的协调工作，你可以将其联想到我们日常经常使用的电源适配器。

IO流中的字节流和字符流的接口不同，他们之间的协调工作就是通过适配器模式完成的。

`InputStreamReader` 和 `OutputStreamWriter` 就是两个适配器(Adapter)， 同时，它们两个也是字节流和字符流之间的桥梁。`InputStreamReader` 使用 `StreamDecoder` （流解码器）对字节进行解码，**实现字节流到字符流的转换，**`OutputStreamWriter` 使用`StreamEncoder`（流编码器）对字符进行编码，实现字符流到字节流的转换。

```java
// InputStreamReader 是适配器，FileInputStream 是被适配的类
InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
// BufferedReader 增强 InputStreamReader 的功能（装饰器模式）
BufferedReader bufferedReader = new BufferedReader(isr);
```

##### 工厂模式

工厂模式用于创建对象，NIO 中大量用到了工厂模式，比如 `Files` 类的 `newInputStream` 方法用于创建 `InputStream` 对象（静态工厂）、 `Paths` 类的 `get` 方法创建 `Path` 对象（静态工厂）、`ZipFileSystem` 类（`sun.nio`包下的类，属于 `java.nio` 相关的一些内部实现）的 `getPath` 的方法创建 `Path` 对象（简单工厂）。

##### 观察者模式

NIO 中的文件目录监听服务使用到了观察者模式。

#### Java 中 3 种常见 IO 模型

##### BIO (Blocking I/O)

BIO属于同步阻塞IO，当应用程序发起read调用后，会一直阻塞，等到内核把数据从内核空间拷贝到用户空间。这种IO方法遇到并发场景较高的时候性能很差。

##### NIO (Non-blocking/New I/O)

同步非阻塞型IO会不断询问内核是否准备好数据，十分浪费CPU资源，因此引入**IO多路复用技术**。在IO多路复用技术中，线程发起一次select系统调用，等待内核准备好数据后，会返回ready指令，去通知线程发起read调用，从而将数据从内核空间拷贝到用户空间。

- **select 调用**：内核提供的系统调用，它支持一次查询多个系统调用的可用状态。几乎所有的操作系统都支持。

- **epoll 调用**：linux 2.6 内核，属于 select 调用的增强版本，优化了 IO 的执行效率

![IO多路复用](.\ref\IO多路复用.png)

##### AIO (Asynchronous I/O)

异步 IO 是基于事件和回调机制实现的，也就是应用操作之后会直接返回，不会堵塞在那里，当后台处理完成，操作系统会通知相应的线程进行后续的操作。

![AIO](.\ref\AIO.png)
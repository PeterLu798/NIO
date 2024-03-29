# NIO

* [1. 概述](#1)
* [2. 缓冲区](#2)
* [3. 通道](#3)
* [4. 选择器与IO多路复用](#4)

<h3 id="1">1. 概述</h3>
<br>&emsp;IO分为BIO、NIO、AIO，即阻塞IO，同步非阻塞IO，异步非阻塞IO。
<br>&emsp;BIO由于阻塞性，一般不适用于设计高性能高并发类产品，尤其一些高性能中间件，比如消息队列等。
<br>&emsp;AIO异步非阻塞，据说由于Linux系统的支持并不是很成熟，在性能上并没有NIO好，因此一些服务器类的开发框架也弃之不用，如Netty。这
也就是说AIO的路还很长。
<br>&emsp;那么当下最适合的用于设计高性能的服务类框架的技术也就只有NIO了。
<br>&emsp;要学习NIO，最关键的几个技术要学会。
<br>&emsp;一是缓冲区的使用。缓冲区的Java实现是Buffer类，它是一个抽象类，总共有7个直接子类：ByteBuffer, CharBuffer, ShortBuffer,
IntBuffer, FloatBuffer, LongBuffer以及DoubleBuffer。本篇主要介绍ByteBuffer和CharBuffer的一些api，其他类的也都差不多。
<br>&emsp;缓冲区的实现有两种方式：第一种是堆缓冲区，第二种是直接缓冲区。堆缓冲区的实质是数组，它受到JVM堆内存的管理。直接缓冲区是建立在直接内存中的缓冲区，
直接内存(Direct Memory)与本地内存(Native Memory)相似，首先它不在JVM堆内存中，因此不会因为GC而释放内存，其次它的大小受到操作系统对整个用户程序（也就是其所属的整个Java应用）
所分配的内存大小的限制。使用直接内存的好处是减少了缓冲区在JVM堆内存和本地内存之间的复制，有助于提高性能。
<br>&emsp;缓冲区相比于BIO中直接使用字节数组还有一个优势就是，缓冲区定义了很多操作数组的方法，这样使用起来非常方便，这些方法我们在后面详细介绍。
<br>&emsp;学习NIO的第二个必学的技术是通道，这是很自然的事情，要把数据从一个缓冲区传输到另一个缓冲区就必须要有一条通道，NIO中这条通道的实现是
java.nio.channels.Channel，这是一个接口。
<br>&emsp;我们一般关心两类通道，一是文件通道FileChannel，一是网络通道SocketChannel、ServerSocketChannel等。
<br>&emsp;文件通道提供了将缓冲区数据写入文件、将文件中的数据读入缓冲区等方法，还提供了文件锁，包括读锁和写锁。还提供了将数据直接写入其他通道
以及从其他通道直接读入此通道的方法，这种技术就是所谓的零拷贝技术，零拷贝技术在很多高性能中间件中得到很广泛的应用，如如Kafka的消费者实现中。
<br>&emsp;网络通道分为服务端通道ServerSocketChannel和客户端通道SocketChannel，服务端通道不仅提供了阻塞模式的接受连接方法，还提供了非阻塞
模式下监听连接的方法，这部分得配合选择器实现，这就是大名鼎鼎的多路复用，这是NIO技术中的核心之一。同理客户端通道也提供了阻塞和非阻塞两种建立连接的
模式。
<br>&emsp;学习NIO的第三个必学的技术就是上面刚提到的选择器Selector。
<br>&emsp;选择器和通道的关系可以这么理解，将多个通道注册到一个单线程的选择器上，并且告诉选择器每个通道感兴趣的事件。一但有感兴趣的事件发生，选择器就会
主动通知相应的通道，然后通道就会处理这个事件了。这种工作模式正是NIO的核心技术：多路复用。多路复用技术通过使用一个单线程来调节多个通道，这样就避免了如果
使用多线程会因上下文切换而产生的较高的性能消耗。
<br>&emsp;NIO的这种高性能的技术被很多时下流行的框架作为底层，其中比较出名的如Netty，而Netty又是其他一些高性能技术的底层核心，如RocketMQ, 
Spring WebFlux, Apache Dubbo, Zookeeper等等。

<h3 id="2">2. 缓冲区</h3>
<br>2.1 基本概念
<br>2.1.1 缓冲区
<br>&emsp;缓冲区就是用来存放数据的缓存区域。其底层实现就是数组。申明缓冲区的几个方法（以ByteBuffer为例）：
* 将数组包装为缓冲区wrap方法
```java
    byte[] bytes = {1,2,3,4,5};
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
```
* 创建堆缓冲区
```java
    //申明容量为10的字节缓冲缓冲区
    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
```
* 创建直接缓冲区
```java
    //申明容量为10的直接缓冲区
    ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(10);
```
* 创建只读缓冲区 asReadOnlyBuffer()
```java
    byte[] bytes = {4,5,6,7,8};
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).asReadOnlyBuffer();
```
<br>2.1.2 缓冲区的容量
<br>&emsp;缓冲区的容量是指可容纳元素的最大数量。
```java
    int capacity() //返回容量
```

<br>2.1.3 缓冲区的限制
<br>&emsp;指第一个不应该读取或写入的位置(index)。
```java
    int limit() //返回限制
    Buffer limit(int newLimit) //设置此缓冲区的限制
```

<br>2.1.4 缓冲区的位置
<br>&emsp;指“下一个”要读取或写入的位置(index)。
```java
    int position() //获取位置
    Buffer position(int newPosition) //设置此缓冲区新的位置
```

<br>2.1.5 剩余空间大小获取
<br>&emsp;剩余空间是指当前位置与limit之间的元素数。
```java
    int remaining() //返回剩余空间大小，该方法源码实现如下：
    public final int remaining() {
        return limit - position;
    }
```

<br>2.1.6 缓冲区的标记
<br>&emsp;标记mark()方法是对“当前位置”进行标记，标记之后当缓冲区的位置再发生变化时，想要回到之前的位置，则可以使用reset()方法。（注意
如果没有进行过标记，直接调用reset()会报错）。如果重新设置position或limit的值小于mark标记过的值时，mark会被丢弃，丢弃后的mark值为-1.
<br>2.1.7 clear方法
<br>&emsp;clear方法的作用是一键还原。其源码实现如下
```java
    public final Buffer clear() {
        position = 0;
        limit = capacity;
        mark = -1;
        return this;
    }
```

<br>2.1.8 flip方法
<br>&emsp;flip()的作用是将limit拉回至position处，然后将position复原到0位置，缩小了limit的范围，相当于subString(0, length)。其源码实现如下：
```java
    public final Buffer flip() {
        limit = position;
        position = 0;
        mark = -1;
        return this;
    }
```
<br>&emsp;flip()的使用场景一般为：当向缓冲区中写入了元素之后，再从缓冲区读取这些元素。伪代码如下：
```java
    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    //写入元素
    byteBuffer.put(1);
    //然后flip
    byteBuffer.flip();
    //后续的读操作或其他操作
```

<br>2.1.9 rewind方法
<br>&emsp;此方法的作用是将position归零，丢弃标记（如果有），但是不改变limit的值。其源码实现如下：
```java
    public final Buffer rewind() {
        position = 0;
        mark = -1;
        return this;
    }
```
<br>&emsp;rewind()方法一般使用场景为重新写入或重新读取。

<br>2.2 ByteBuffer类的主要方法
<br>2.2.1 读写方法
* 相对位置的读写方法
<br>单个元素读写方法，会使position加1
<br>put(byte b) 
<br>byte get()
<br>批量读写方法，会使position加length
<br>put(byte[] src, int offset, int length)
<br>get(byte[] dst, int offset, int length)
<br>put(byte[] src) 相当于put(src, 0, src.length)
<br>get(byte[] dst) 相当于get(dst, 0, dst.length)
<br>注意这些批量读写方法都要保证缓冲区的remaining()大于等于数组的length，否则会报错。
<br>put(ByteBuffer src) 
<br>此方法相当于put(byte[] src)，将缓冲区src的remaining()之间的元素写入此缓冲区的remaining()之间，
此方法也要保证此缓冲区的remaining()大于等于 src.remaining()
<br>putChar(char value): position会增加2【因为char占用2个字节】
<br>putShort(short value): position会增加2
<br>putInt(int value): position会增加4
<br>putFloat(float value): position会增加4
<br>putLong(long value): position会增加8
<br>putDouble(double value): position会增加8
* 绝对位置的读写方法，这些方法不会改变缓冲区的位置
<br>get(int index)
<br>put(int index, byte b)
<br>putChar(int index, char value)
<br>putShort(int index, short value)
<br>putInt(int index, int value)
<br>putFloat(int index, float value)
<br>putLong(int index, long value)
<br>putDouble(int index, double value)

<br>2.2.2 复制缓冲区
* 复制缓冲区的剩余空间
<br>以下这这方法都是复制缓冲区的:
<br>ByteBuffer slice()
<br>CharBuffer asCharBuffer()
<br>ShortBuffer asShortBuffer()
<br>IntBuffer asIntBuffer()
<br>FloatBuffer asFloatBuffer()
<br>LongBuffer asLongBuffer()
<br>DoubleBuffer asDoubleBuffer()
<br>核心要点如下：
<br>(1) 新缓冲区的内容是从此缓冲区的当前position开始，新缓冲区的limit为此缓冲区的remaining()的 1/n ，n为进制数，slice()方法可以理解为 n=1
<br>(2) 新缓冲区与原缓冲区共享底层数组，因此：
<br>&emsp;1> 原缓冲区元素的更改在新缓冲区中是可见的，反之亦然
<br>&emsp;2> 当且仅当原缓冲区是直接缓冲区时，新缓冲区才是直接缓冲区
<br>&emsp;3> 当且仅当原缓冲区是只读缓冲区时，新缓冲区才是只读的
<br>(3) 新缓冲区的position、limit、mark与原缓冲区时独立的。新缓冲区的初始position是0，limit为原缓冲区的remaining()大小，mark是-1
* 缓冲区的快照
<br>ByteBuffer duplicate(): 复制一个与原缓冲区一模一样的缓冲区，包括position, limit, capacity, mark都相同，这两个缓冲区共享数组，但是position, limit, mark都是相互独立的

<br>2.2.3 解决中文乱码问题
<br>&emsp;乱码的原因是编码不一致，解决办法就是使编码对称。
<br>(1) 在存、取时都使用 utf-16BE 编码，因为get方法无法指定编码格式，因此我们在写的时候可以指定
```java
    "中文乱码".getBytes("utf-16BE")
```
这样在get时就不会乱码
<br>(2) 使用decode转码让编码对称
```java
    CharBuffer buffer = Charset.forName("utf-8").decode(byteBuffer);
```

<br>2.2.4 比较缓冲区
<br>&emsp;比较缓冲区的方法是 equals方法和compareTo方法
<br>(1) 相同点：都是比较remaining()之间的元素是否相等
<br>(2) 不同的：equals返回true/false，compareTo返回int: 0表示相等，非0表示不相等，大于0表示this.remaining() < that.remaining()

<h3 id="3">3. 通道</h3>
<br>3.1 基本概念
<br>&emsp;
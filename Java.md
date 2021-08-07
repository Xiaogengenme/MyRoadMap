# Java内存空间



# Java里面的数据结构

## HashMap

### 1. HashMap的特点

### 2. 存储结构

数组 + 链表 + 红黑树

<img src="https://img-blog.csdn.net/20170803204952538?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbG9naW5fc29uYXRh/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast" alt="存储结构" style="zoom:75%;" />

数组就是指Node[] table数组，Node是：

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;    //用来定位数组索引位置：即hashCode，算出来之后就不变了
    final K key;
    V value;
    Node<K,V> next;   //链表的下一个node

    Node(int hash, K key, V value, Node<K,V> next) { ... }
    public final K getKey(){ ... }
    public final V getValue() { ... }
    public final String toString() { ... }
    public final int hashCode() { ... }
    public final V setValue(V newValue) { ... }
    public final boolean equals(Object o) { ... }
}
```

构造HashMap的几个重要参数：

```java
int threshold;             // 扩容阈值：loadFactor * 的结果
final float loadFactor;    // 负载因子：0.75默认
transient int modCount;  // 出现线程问题时，负责及时抛异常
transient int size;     // HashMap中实际存在的Node数量
```

> modCount的作用：

我们知道java.util.HashMap不是线程安全的，因此在使用**迭代器Iterator**的过程中，如果有其他线程修改了map，将抛出ConcurrentModificationException，这就是所谓fail-fast策略。**这一策略在源码中的实现就是通过modCount**，它记录修改次数，在迭代器初始化过程中会将这个值赋给迭代器的expectedModCount，在迭代过程中，判断modCount跟expectedModCount是否相等，如果不相等就表示已经有其他线程修改了Map。所以遍历那些非线程安全的数据结构时，尽量使用迭代器Iterator。

> 长度为什么非要是2的次幂数呢？（初始16）

**在HashMap中，哈希桶数组table的长度length大小必须为2的n次方(一定是合数)，这是一种非常规的设计**

常规的设计是把桶的大小设计为素数。相对来说素数导致冲突的概率要小于合数，Hashtable初始化桶大小为11，就是桶大小设计为素数的应用（Hashtable扩容后不能保证还是素数）。HashMap采用这种非常规设计，**主要是为了在取模和扩容时做优化，同时减少冲突**，HashMap定位哈希桶索引位置时，也加入了高位参与运算的过程。









# Java异常

异常主要分为三类：

1. 检查性异常：这种异常通常是由于一些显式的错误产生，比如少写了;，或者输入的文件不存在，这种异常在编译时不会被忽略。
2. 运行时异常：运行时异常通常会被编译器忽略（不知道有什么例子）
3. 错误：错误不是异常，是程序员避免不了的（编码层面），比如堆栈溢出，属于错误不是异常。在编译时不能被发现。

### 1. Exception类：

![image-20210727155304149](/Users/xiaogengen/Library/Application Support/typora-user-images/image-20210727155304149.png)

### 2. 捕获异常

* catch 不能独立于 try 存在。
* 在 try/catch 后面添加 finally 块并非强制性要求的。
* try 代码后不能既没 catch 块也没 finally 块。

### 3. 自定义异常

- 所有异常都必须是 Throwable 的子类。
- 如果希望写一个检查性异常类，则需要继承 Exception 类。
- 如果你想写一个运行时异常类，那么需要继承 RuntimeException 类。

### 4. ReentrantLock加锁与释放锁时对于异常的处理

ReetrantLock在使用时要记得在finally块中添加解锁操作，以防在执行过程中出现错误抛出异常，没有解锁的问题。

```java
Class X {
  private final ReentrantLock lock = new ReentrantLock();
  
  public void f() {
    // 是在try的外面先加锁！！！！tmd！！！
    lock.lock();
    try {
      // do something
    } catch (Exception e) {
      
    }finally {
      lock.unlock();
    }
  }
}
```

### 5. 开发中对于捕获异常的一些思路方法

[java基础（十）捕获异常还是抛出异常_miracle_8的博客-CSDN博客_service层异常抛出还是捕获](https://miracle-j.blog.csdn.net/article/details/78285940?utm_medium=distribute.pc_relevant_t0.none-task-blog-2~default~BlogCommendFromMachineLearnPai2~default-1.control&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2~default~BlogCommendFromMachineLearnPai2~default-1.control)

在开发中，我们可以自己控制的一场主要是前面两种，

可检查的异常和运行时异常

可检查异常在编译时就会被发现，不涉及异常捕获的问题

运行时异常RunTimeException通常会被编译器忽略，需要我们手动捕获，进行处理

RunTimeException可能包括：空指针异常、数组越界异常、除数为0异常等

#### 处理异常的原则之一：延迟捕获

意思是，当异常发生时，不应立即捕获，而是应该考虑当前作用域是否有有能力处理这一异常的能力，如果没有，则应将该异常继续向上抛出，交由更上层的作用域来处理。

一个例子：某方法String readFile(String filename)，会去尝试读出指定文件的内容并返回，其使用FileInputStream来读取指定文件，而FileInputStream的构造方法会抛出FileNotFoundException，这是一个Checked Exception。那么readFile方法是应该捕获这个异常，还是抛出这个异常呢？很显然应该抛出。因为readFile这个方法可能会在不同的场景下，被不同的代码调用，在这些场景中，出现“文件未找到”的情况时的处理逻辑可能是不同的，例如某场景下要发出告警信息，另一场景下可能会尝试从另一个文件中读取，第三个场景下可能需要将错误信息提示给用户。在这种情况下，在readFile方法内的作用域中，是处理不了这个异常的，需要抛出，交由上层的，具备了处理这个异常的能力的作用域来处理。

对于MVC框架中：

1. **controller层必须捕获异常**，*一般情况下不允许将系统内部的异常不做任何封装处理直接抛给客户端，这样对系统来说会暴露过多信息（异常栈信息都抛给客户端，可能会把SQL结构都抛出去），这是不安全因素*；同时，这对用户来说也是不友好的。
2. service层，业务层需要处理业务逻辑，当然这一层不需要考虑参数是否为空，需要考虑的仅仅是从dao查询相关的异常，比如ConnectionExcepion、SQLException、BadSQLGrammerException等异常，这些异常我通常会去捕获，因为这些异常涉及到业务逻辑是否能正常执行，确实是service该考虑的事。
3. **dao层**，好像没啥异常....

#### 先捕获小的，再捕获大的！！

### 6. 线程池的异常处理

超级好的文章：[线程池异常处理详解,一文搞懂_hello world-CSDN博客_线程池抛出异常](https://blog.csdn.net/qq_20009015/article/details/100569976)



# 并发

## CAS

以下这段代码有什么问题？

```java
public class Singleton {
    private AtomicReference<Singleton> instance = new AtomicReference<>();
 
    public Singleton getSingleton(){
        Singleton result = instance.get();
        if(instance.compareAndSet(null,new Singleton())){
            result = instance.get();
        }
        return result;
    }
     
}
```





# 线程池

## 线程池基本原理

### 1. 为什么要使用线程池？

### 2. 线程池对任务的处理流程⭐️

![截屏2021-08-05 上午12.05.49](/Users/xiaogengen/Desktop/秋招/MyRoadMap/Java.assets/截屏2021-08-05 上午12.05.49.png)

```java
public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
        /*
         * Proceed in 3 steps:
         *
         * 1. 如果正在运行的线程少于 corePoolSize，请尝试使用给定命令作为其第一个任务启动一个新线程。 对 addWorker 的调用以原子方式检查 runState 和 workerCount，从而通过返回 false 来防止在不应该添加线程时出现误报。
         * 2. 如果任务可以成功排队，那么我们仍然需要仔细检查是否应该添加一个线程（因为自上次检查以来现有线程已死亡）或线程池自进入此方法后关闭。 因此，我们重新检查状态，并在必要时在停止时回滚入队，如果没有则启动一个新线程。
         * 3. 如果我们无法排队任务，那么我们尝试添加一个新线程。 如果它失败了，我们知道我们已经关闭或饱和，因此拒绝该任务。
         */
        int c = ctl.get();
    	// 1.
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
    	// 2.
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            if (! isRunning(recheck) && remove(command))
                reject(command);
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
    	// 3.
        else if (!addWorker(command, false))
            reject(command);
    }
```

#### 工作线程：

线程池创建线程时， 会将线程封装成工作线程Worker，Worker执行完任务后，还会循环地获取工作队列中的任务来执行。

线程池中的线程执行任务分为两种情况：

1. 在`execute()`方法中创建一个线程时，会让这个线程执行当前的任务；
2. 在这个线程执行完当前任务之后，Worker会反复从队列中获取任务来执行。

### 3. 线程池的使用

#### 1、线程池的创建，关键参数⭐️

```java
new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, milliseconds, runnableTaskQueue, handler);
```

* corePoolSize：核心线程数
* maximumPoolSize：最大线程数
* runnableTaskQueue：用于保存等待执行任务的阻塞队列
  * ArrayBlockingQueue：基于数组的**有界**阻塞队列，FIFO
  * LinkedBlockingQueue：基于链表的阻塞队列，FIFO，吞吐量（Executors.newFixedThreadPool)
  * SynchronousQueue：一个不存储元素的阻塞队列，每插入一个元素必须等到另一个线程调用移除操作，否则一直阻塞在插入中。吞吐量通常高于LinkedBlockingQueue(Executors.newCachedThreadPool)
  * PriorityBlockingQueue：一个**具有优先级的**，**无限**阻塞队列
* RejectedExecutionHandler：饱和策略
  * AbortPolicy：直接抛出异常
  * CallerRunsPolicy：用调用者所在线程运行任务
  * DiscardOldestPolicy：丢弃最近的一个任务，并执行当前任务
  * DiscardPolicy：不处理直接丢掉，也不抛出异常
* ThreadFactory
* keepAliveTime：工作线程空闲的时间
* TimeUnit：时间单位

#### 2、向线程池提交任务

主要有两种方法：submit 和 execute

execute方法用于提交不需要返回值的任务，所以无法判断任务是否被线程池成功执行

```java
threadPool.execute(new Runnable() {
    @Override
    public void run() {
        // do something
    }
});
```

submit方法用于提交需要返回值的任务，线程池会返回一个future类型的对象，通过future对象可以判断任务是否执行成功。

调用future的get()方法可以获取到future对象返回的东西，但是线程如果没有执行结束，这个get方法会阻塞。我们可以使用get(long timeout, TimeUnit unit)方法来设置阻塞时间

```java
Future<Object> future = executor.submit(hasRetturnValuetask);
try {
    Object o = future.get();
} catch (InterruptedException e) {
    // 处理中断异常
} catch （ExecutionException e) {
    // 处理无法执行任务异常
} finally {
    executor.shutdown();
}
```

#### 3、关闭一个线程池

可以通过调用线程池的shutdown或者shutdownNow方法来关闭线程池

这两个方法的原理都是遍历线程池中的工作线程，然后逐个调用线程的interrupt方法来中断线程，所以无法响应中断的线程可能永远停止不了。

区别是：shutdownNow首先将线程池的状态设置成STOP，然后尝试停止所有正在执行或者暂停任务的线程，并返回等待执行任务的列表。

showdown方法将线程池的状态设置为SHUTDOWN，然后中断所有没有正在执行任务的线程。

#### 4、合理地配置线程池

有几个角度可以作为配置线程池的考虑角度：

* 任务的性质：CPU密集型任务、IO密集型任务和混合型任务
* 任务的优先级：高中低
* 任务执行的时长：长中短
* 任务的依赖性：是否依赖于其他系统资源，如数据库连接

CPU密集型的任务尽量配置小线程池，可以配合CPU数量+1的线程池

IO密集型CPU不是一直在执行任务，可以稍微多一点配置值CPU数量*2的线程池

**建议使用有界队列**

#### 5、线程池的监控

## Executor框架












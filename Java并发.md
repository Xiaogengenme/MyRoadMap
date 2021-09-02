## 02 悲观锁机制

![image-20210831115412936](https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/202108311154211.png)

线程私有的部分不会出现并发问题，线程共享的区域：堆（存放对象），方法区（存放类加载信息，常量，静态变量）在出现多线程的竞争时会出现并发问题。

Java中锁的实现主要分为两种，基于Object的悲观锁和基于CAS的乐观锁。

在Java中，每个对象都可以拥有一把锁，锁的记录存放在对象头中，记录了当前对象是被哪个线程占用的。

### 对象头

##### 对象头

Java对象 = 对象头 + 实例数据 + 对齐填充字节

对其填充字节的作用是保证Java对象的大小是8字节的倍数。

其中对象头又分为：Class Pointer和Mark Word两个部分

Class Pointer是一个指针，指向当前对象类型所在方法区中的Class信息。

Mark Word对于锁问题比较重要，一般包括对象的运行时信息：

<img src="https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/202108311624991.png" alt="截屏2021-08-31 下午4.24.45"  />

### Synchronized

#### Synchronized的作用原理

Synchronized通过生成字节码指令Monitor Enter和Monitor Exit来控制线程的同步。

![截屏2021-08-31 下午4.27.38](https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/202108311627633.png)

#### Synchronized的性能问题

Synchronized的执行原理是在需要加锁的指令集前后添加Monitor Enter和Monitor Exit两条字节码指令，这两条字节码指令的执行下层是通过操作系统的mutex lock指令来执行，Java的线程实际上是操作系统线程的映射，如果要对线程进行挂起和唤醒操作，要进行加锁解锁操作时，需要将操作系统从用户态转换为内核态，这个操作是很耗时的。

#### JDK1.6对Synchronized的优化（锁升级过程⭐️）

<img src="https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/202108311624991.png" alt="截屏2021-08-31 下午4.24.45"  />

##### 无锁

1. 对象的访问不会出现在多线程环境下或者出现在多线程环境下也不会出现多线程竞争的问题
2. 存在多线程竞争，但是我们使用非锁方式，比如CAS

##### 偏向锁

共享资源对象认识线程，倾向于每次将锁资源分配给这个线程。

我们通过锁标志位来判断当前对象是不是处于偏向锁状态，如果是则通过对象头中的线程ID字段来判断是不是我们偏向的那个线程，如果不是，**说明有不止一个线程竞争当前锁资源，偏向锁升级为轻量级锁。**

##### 轻量级锁

当一个线程将要获取同步资源时，如果这时同步对象没有被锁定（锁标志位为‘’01’‘）那么虚拟机首先在线程的栈帧中开辟一块区域是Lock Record，Lock Record一部分用于保存锁对象的对象头中Mark Word的拷贝，帮这个同步对象先存着。另一部分是owner指针指向锁对象。

然后，**线程使用CAS操作尝试将对象头中的Mark Word区域更新为指向Lock Record的指针**，如果更新成功了，代表这个线程拥有了这个对象的锁。这是锁标志位变为轻量级锁00。但如果更新失败了，证明有其他线程对当前对象竞争，那么首先要判断是不是自己获取的，因为Synchronized是可重入锁，线程可以直接进入同步代码区，如果不是，说明已经有其他线程获取了该锁，本线程**进入自旋等待**。

**如果有超过一个线程在自旋等待，轻量级锁则膨胀为重量级锁。**

##### 重量级锁

![截屏2021-08-31 下午4.57.52](https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/202108311657767.png)





## 03 乐观锁机制

### 乐观锁使用的场景

在一些情况下，同步代码块的执行耗时远远小于线程切换的耗时，比如一些读操作比较多的场景。

#### Java中CAS的实现

原子类AtomicInteger，成员变量包括Unsafe类型的实例和一个Long类型的offset

Unsafe的CAS操作来对值进行更新操作

调用AtomicInteger的incrementAndGet方法，会调用Unsafe对象的getAndAddInt方法，在之后调用了Unsafe的compareAndSwapInt方法（本地方法，有native修饰符），这个就是CAS操作，如果修改不成功则进入自旋。

自旋默认10次结束，可以自己再调整。



## AQS

### AQS的概念

AQS，全称是Abstract Queued Synchronizer，**AQS通过封装CAS操作来实现对共享资源的同步状态进行管理的框架**。向下封装了CAS操作，定义了获取和修改同步状态的方法，另一方面又利用模板设计模式对上层开发提供了可以继承复用的方法，使上层可以自定义设计出不同的锁的模式，如ReentrantLock、CountDownLatch等。

**AQS使用一个volatile修饰int类型的同步状态，和一个内置的FIFO队列来完成线程对于共享资源获取的排队工作，通过CAS来修改同步状态。**

![截屏2021-09-01 上午10.38.59](https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/202109011039124.png)

### AQS的设计

#### 成员属性

```java
private volatile int state;
```

state之所以是int：

这里要说到资源的独占模式和共享模式。

```java
private transient volatile Node head;
private transient volatile Node tail;
```

#### 内部类

![截屏2021-09-01 上午10.47.39](https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/202109011047118.png)

Node的成员变量如上。主要包括前后指针，和包装进来的Thread，还有waitStatus是一个枚举值表示当前的状态，状态值就是上面这四种。

#### 成员方法

独占式为例：

##### 尝试获取锁：不管成功还是失败，立即返回。

```java
protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }
```

这一行实现，抛出了一个异常，代表AQS的上层必须要重写这个方法，不然执行AQS自己的逻辑就会直接抛出UnsupportedOperationException异常。

为什么一定要继承类重写？

因为上层的很多逻辑都是基于tryAcquire进行实现的，如果tryAcquire返回true，线程成功获取到锁，上层可以对共享资源进行操作。如果返回false就要判断上层是否要等待，如果不想等待，则需要进行相应的处理

> 这个相应的处理是写在tryAcquire里的吗？

##### 获取锁：愿意进入队列进行等待，直到获取锁。

如果可以愿意等待可以直接调用acquire方法，acquire方法一定能获取到锁：

```java
public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
```

acquire的修饰符是final的，不允许继承类重写，继承类直接用就好。

其中`!tryAcquire(arg)`判断是否获取到锁，如果返回true则获取到锁，可以操作同步对象，如果没有则返回false，接下来判断

`acquireQueued(addWaiter(Node.EXCLUSIVE), arg))`

```java
/*
将线程打包成Node并指定mode
*/
private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode);
    // 接下来的操作就是将Node插入到等待队列的队尾
        // Try the fast path of enq; backup to full enq on failure：先尝试一次快速的入队，如果失败则进完整的入队
        Node pred = tail;	// 获取当前尾结点
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {  // 将当前node设置为尾结点
                pred.next = node;  // 如果设置成功将之前的尾结点设置为当前结点的前驱结点
                return node;
            }
        }
        enq(node);  // 如果队列为空，或者cas设置尾结点失败，则进行完整的入队
        return node;
    }
```

完整的入队操作：

```java
/**
     * Inserts node into queue, initializing if necessary. See picture above.
     * 将结点加入队列，如果队列为空则创建一个新的队列
     * @param node the node to insert
     * @return node's predecessor
     */
private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) { // Must initialize
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }
```

这个操作外层有一个无限循环，代表一直尝试将结点插入到尾结点。

插入操作首先判断队列是否为空，如果为空则将结点CAS插入到队列并且设置为head，同时也是tail

如果不为空则CAS设置为尾结点。

最后一定能成功。

接下来的acquireQueued方法就是如果选择下一个自旋等待的Node获取同步资源的方法：

```java
final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

> ⭐️⭐️acquireQueued方法解释
>
> 外层就是指acquireQueued方法中的无限循环，
>
> acquireQueued方法实际上就是AQS中的Node自旋等待获取锁的方法：无限循环里面分为三个部分。之前有说过，
>
> #### 第一部分
>
> 就是自旋判断条件：***p == head && tryAcquire(arg)***，判断前序结点是不是头结点，如果是则获取同步资源，获取成功后将自己设置为头结点，并返回自己的interrupted状态，如果在挂起阶段已经被interrupted了就要在上层acquire方法中执行selfInterrupt
>
> #### 第二部分
>
> 是在插入节点失败之后判断是否需要将线程挂起，挂起的条件上面有提到过，
>
> 首先是进行shouldParkAfterFailedAcquire判断方法：这个方法主要是通过判断前驱结点的waitStatus来进行判断，如果前驱结点的状态是SIGNAL则当前结点可以放心地挂起，返回true，如果前驱结点的waitStatus的状态>0，代表前驱是头结点（头结点不用wait了，所以waitStatus是CANCEL）或者前驱结点线程被中断结点出队，当前结点不能被挂起了，挂起了等会儿可能没人signal它，返回false，
>
> 返回true的话之后会调用parkAndCheckInterrupt，这个方法调用Java native方法LockSupport.park(this)将线程挂起，并返回当前线程的interrupt标志值，如果interrupt也返回true就会接下来执行将interrupt改为true，在下一次自旋获取到锁之后返回给上层函数acquire
>
> 返回false的话会进入下一次自旋的循环，指的就是acquireQueued方法的无限获取循环。
>
> #### 第三部分
>
> 如果中间出错了跳出了循环，进入了finally块，两种可能，可能是中间出错了跳出了循环，或者是return了结果，进入了finally块，这时候判断failed字段，如果是true，就cancelAcquire，如果是false，就是成功获取锁返回了，不进入if。

* 看完上面的解析，大概知道了：如何判断哪些线程需要被挂起，哪些线程需要自旋等待？

这个方法主要是通过判断前驱结点的waitStatus来进行判断，如果前驱结点的状态是SIGNAL则当前结点可以放心地挂起，返回true，如果前驱结点的waitStatus的状态>0，代表前驱是头结点（头结点不用wait了，所以waitStatus是CANCEL）或者前驱结点线程被中断结点出队，当前结点不能被挂起了，挂起了等会儿可能没人signal它，返回false，返回false的话会进入下一次自旋的循环，指的就是acquireQueued方法的无限获取循环。

方法就不粘贴了不好读。

###### 中断和挂起的区别

线程的中断是Java提供的处理线程的一种方法，线程不是一经中断就立即停止，

* 如果线程在等待状态，调用线程interrupt方法会抛出线程的中断异常
* 如果线程在运行状态，调用线程的中断并不会立即停止线程而是修改线程中的isInterrupted的字段，等线程运行结束后抛出中断异常。

在AQS中，线程如果处于挂起状态，是没办法响应中断异常的。需要等到线程被唤醒之后来判断线程是否已经被中断。

#### 释放同步资源

```java
public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }
```

当前头结点结束后释放资源，如果成功释放则唤醒后续结点，如果失败返回false。

其中tryRelease方法是与tryAcquire方法同样的，可以供继承的实现进行重写的方法：

```java
protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }
```

接下来可以仔细看一下唤醒后续结点的unparkSuccessor方法：

```java
private void unparkSuccessor(Node node) {
        /*
         * If status is negative (i.e., possibly needing signal) try
         * to clear in anticipation of signalling.  It is OK if this
         * fails or if status is changed by waiting thread.
         */
        int ws = node.waitStatus;
        if (ws < 0)
            compareAndSetWaitStatus(node, ws, 0);

        /*
         * Thread to unpark is held in successor, which is normally
         * just the next node.  But if cancelled or apparently null,
         * traverse backwards from tail to find the actual
         * non-cancelled successor.
         */
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        if (s != null)
            LockSupport.unpark(s.thread);
    }
```

获取head的waitStatus，如果不为0，那么将其置为0，代表锁已释放。

接下来获取头结点的后继节点，如果后继节点不为空，那么将后继节点唤醒，使用LockSupport.unpark(s.thread);

如果后继节点为空，那么从尾结点往前遍历，找到最靠前的状态不是CANCELED的结点，将其唤醒。

* 为什么是从后往前找最前面的？

与addWaiter方法中，前后两个结点建立连接的顺序有关。顺序是：

1. 后结点的pre先指向前结点
2. 前结点的next指向后结点

这两步在多线程环境下不是原子的，如果唤醒是从前往后找，可能前结点的next还没建立好，搜索可能会中断？？？



#### 独占式总结

> 在获取同步状态时，同步器维护一个同步队列，获取状态失败的线程都会被加入到队列中并在队列中进行自旋；
>
> 移除队列（或停止自旋）的条件是前驱结点为头结点且成功获取了同步状态。
>
> 在释放同步状态时，同步器调用tryRelease方法释放同步状态，并唤醒头结点的后继节点。
>
> 《Java并发编程的艺术》

### 涉及的一些多线程基础知识

#### 理解中断

如果是一个运行的线程，被调用interrupt方法会终止运行

如果通过sleep或者wait方法进入等待状态的线程，被调用interrupt方法，会直接抛出interrupt异常

如果是通过LockSupport.park()方法被挂起的线程，被调用interrupt方法之后更改其中的interrupt标志位，并不会抛出异常。

线程通过`isInterrupted()`方法可以判断线程是否被中断，也可以调用静态方法`Thread.interrupted()`

如果是一个运行的线程被interrupt了，之后在获取isInterrupted标志位时，会返回true

但如果一个处于终止状态的线程，或者是sleep状态的线程被interrupt，抛出中断异常之后，线程的isInterrupted标志位会被清除，再获取时会返回false

#### 线程的状态转移

![Java 线程状态变迁 ](https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/202109012356198.png)

#### wait和sleep的区别

* 两者最主要的区别在于：**`sleep()` 方法没有释放锁，而 `wait()` 方法释放了锁** 。
* 两者都可以暂停线程的执行。
* `wait()` 通常被用于线程间交互/通信，`sleep() `通常被用于暂停执行。
* `wait()` 方法被调用后，线程不会自动苏醒，需要别的线程调用同一个对象上的 `notify() `或者 `notifyAll()` 方法。`sleep() `方法执行完成后，线程会自动苏醒。或者可以使用 `wait(long timeout)` 超时后线程会自动苏醒。



### ReentrantLock

#### ReentrantLock的设计

以AQS最典型的实现ReentrantLock为例，ReentrantLock通过AQS的特性实现了一个独占式的，可重入的锁，并且可以实现公平和非公平锁。具体的实现方式是在ReentrantLock的实现中首先实现一个继承AQS的子类Sync，定义一些模版方法，然后再设计Sync的两个子类FairSync和NonFairSync

其中，非公平锁获取锁资源会首先尝试插队，如果失败则进入阻塞队列排队等待

```java
// 写得非常好的伪代码
static final class NonfiarSync extends Sync {
  final void lock() {
    if (compareAndSetState(0, 1)) {
      setExclusiveOwnerThread(Thread.currentThread());
    } else {
      acquire(1);
    }
  }
}
```

公平锁直接进入排队队列进行等待

```java
static final class FairSync extends Sync {
  final void lock() {
    acquire(1);
  }
}
```

acquire方法一定会获取到锁！！

#### ReentrantLock与Synchronized的区别

* 两者都是可重入锁
* 实现层面不一样，Synchronized关键字是JVM底层通过Monitor enter和Monitor exit指令实现的，而ReentrantLock是上层通过API实现的。
* ReentrantLock相比于Synchronized增加了一些高级功能，包括：
  1. **等待可中断** : `ReentrantLock`提供了一种能够中断等待锁的线程的机制，通过 `lock.lockInterruptibly()` 来实现这个机制。也就是说正在等待的线程可以选择放弃等待，改为处理其他事情。
  2. **可实现公平锁** : `ReentrantLock`可以指定是公平锁还是非公平锁。**而`synchronized`只能是非公平锁**。所谓的公平锁就是先等待的线程先获得锁。`ReentrantLock`默认情况是非公平的，可以通过 `ReentrantLock`类的`ReentrantLock(boolean fair)`构造方法来制定是否是公平的。
  3. **可实现选择性通知（锁可以绑定多个条件）**: `synchronized`关键字与`wait()`和`notify()`/`notifyAll()`方法相结合可以实现等待/通知机制。`ReentrantLock`类当然也可以实现，但是需要借助于`Condition`接口与`newCondition()`方法。

#### ReentrantLock如何进行加锁解锁？

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

## 












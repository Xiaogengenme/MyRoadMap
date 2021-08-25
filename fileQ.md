# 计算机网络

## 七层与四层结构

## HTTP

1. HTTP的请求过程⭐️
   * DNS的请求流程
2. 常见的HTTP状态码
3. HTTP请求方法：GET与POST的区别⭐️
4. HTTP缓存策略
   * E-tag标志位
5. HTTP版本迭代
   * HTTP1.0的缺点
   * HTTP1.1对HTTP1.0的升级
   * HTTP2.0对HTTP1.1的升级
   * HTTP2.0的局限性
6. 网页cookie和session的区别
7. HTTPS建立连接的过程⭐️
8. 对称加密与非对称加密的区别
9. 常见的对称加密和非对称加密算法

## TCP

1. TCP三次握手
2. 三次握手的序列号一般是从多少开始的？
3. 为什么说TCP是一个可靠的协议？
4. TCP的拥塞控制⭐️
5. TCP的流量控制
6. TCP与UDP的区别，使用场景

## RPC

1. RPC的请求流程
2. 对美团Thrift RPC框架的理解⭐️

# Java

## Java基础

1. 对Java面向对象编程的特性的理解⭐️
2. 对多态的理解：运行时多态、编译时多态？
3. 对封装的理解
4. 重载与重写的区别
5. final关键字的作用⭐️
6. transient关键字的作用
7. static关键字的作用⭐️
   * static字段如何进行序列化
8. String、StringBuffer、StringBuilder的区别于比较
9. 自动装箱与拆箱
10. 抽象类与接口的区别
11. Java中的异常
    * 异常分为哪几种？继承关系？
    * 在调用函数中出现的异常如果本层函数不能处理的话应该如何处理？
    * 线程池出现异常如何处理？
    * RPC调用出现异常需要如何处理？REST调用出现异常该如何处理？
12. 深拷贝与浅拷贝的区别？
13. Java反射机制

## 集合

1. List、Set、Map三者的特别区别
2. LinkedList与ArrayList
3. ArrayList底层采用了什么数组？

### HashMap⭐️

1. HashMap的结构
2. HashMap的put流程⭐️
3. HashMap的扩容机制
   * 先插入还是先扩容？
   * 为什么每次扩容扩两倍？
4. HashMap线程不安全会出现的问题

### LinkedHashMap

1. LinkedHashMap的结构

### ConcurrentHashMap⭐️

1. **ConcurrentHashMap的结构设计⭐️**
2. **ConcurrentHashMap插入元素的流程，会经过几次hash？⭐️**
3. **ConcurrentHashMap的get操作**
4. **ConcurrentHashMap的size操作**

## Java同步原理

### Java多线程的基础知识

notify与notifyAll的区别？

### volatile

1. volatile关键字的作用？
2. volatile关键字实现单例模式
3. volatile可见性的实现原理

### Synchronized

1. synchronized的使用方法

2. synchronized关键字的底层原理

3. Java中锁的升级过程⭐️

4. 为什么线程之间切换的效率较低？

5. 如果解决线程切换太频繁的问题？

   无锁编程、使用最少线程、协程、CAS

### CAS

1. CAS的原理
2. Java中如何实现CAS？（原子类）

### AQS

1. 讲一讲对AQS的理解？
2. AQS的结构（含有多少条队列？)
3. 有哪些基于AQS的实现类

### ReentrantLock

1. ReentrantLock的设计方式

2. ReentrantLock如何进行加锁解锁？

3. ReentrantLock与Synchronized的区别

   非阻塞等待、公平策略、可重入、可结合Condition

### Java线程池

1. 使用线程池的好处？为什么要使用线程池？
2. 创建线程池的主要参数
3. 线程池执行任务的流程
4. 线程池的创建方法
5. 线程池大小的设置



## JVM

### Java内存结构

### Java中的垃圾收集机制

1. 什么对象需要回收？

2. 什么时候进行垃圾回收？

3. 如何回收？

   * 几种垃圾回收算法

   * 讲一讲熟悉的垃圾收集器？⭐️

     CMS的运行过程，与G1的区别，CMS为什么不整理内存？

### 类加载机制

1. **类加载的流程⭐️**

2. **new一个对象需要经过几步？**

3. **讲一讲双亲委派机制？⭐️**

4. **如何打破双亲委派机制？⭐️**

   **覆写类加载器loadClass和findClass？**





# 操作系统

## 进程与线程管理

1. 并发与并行的区别
2. 进程、线程、协程的关系⭐️
3. **进程与线程的生命周期、状态**
4. 进程调度算法
5. **进程和线程进行切换时的流程⭐️**
6. **进程之间的通信方式⭐️**
7. **线程之间的通信方式**⭐️
8. 线程过多会发生什么问题？

### 死锁

1. 死锁产生的原因
2. 如何解决死锁

## IO

1. IO的概念？
2. IO中的多路复用？⭐️
   * select、poll、epoll的关系与区别
3. 同步、异步、阻塞、非阻塞⭐️



## 内存管理

1. **分段式管理**
2. **内存分配算法**
3. **分页式管理**
4. **分段式管理**
5. **段页式管理**
6. **虚拟内存⭐️**
7. **页面调度算法**
8. **内存抖动现象**



# 数据库

## 引擎

1. InnoDb与MyISAM有什么区别？⭐️

## 索引

1. 为什么使用B+树？
2. B+树与B树的区别？
3. 什么样的情况下查询不走索引？⭐️
4. 索引的类型？⭐️
   * 主键索引
   * 普通索引
   * 唯一索引
   * 聚簇索引与非聚簇索引
5. 普通索引与唯一索引该如何选择？⭐️
6. 自增主键索引的好处（⭐️shopee）
7. 

## 事务

### 事务隔离

1. 四种事务隔离机制
2. 事务的特点
3. 如何实现事务隔离？
4. MySQL如何实现MVCC？

### 日志

1. redo log是什么有什么作用？⭐️
2. bin log是什么有什么作用？⭐️
3. 更新操作的流程是怎样的？（两阶段提交）⭐️
4. 

## 锁

## 分库分表

## 读写分离与主从同步



# Spring问题

1. Spring中Bean的加载流程
2. Spring中Bean的生命周期
3. 谈一谈Spirng IoC
4. Spring中的类加载器
5. Spring Boot的启动流程
6. Spring Boot的自动装配原理
7. Spring中工厂设计模式的应用
8. Spring中如何解决循环依赖问题
9. Spring Boot、Spring MVC在处理网络请求的时候的处理流程
10. 讲一讲Spring AOP是做什么的
11. Spring AOP的实现原理Spring中的事务


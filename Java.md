# Java基础知识

## Java中常见的关键字

### static

四种应用场景：

* static修饰成员变量和成员方法

**被static修饰的成员方法属于类，不属于该类的某个对象，被类中的所有对象共享。可以并且建议通过类名来调用。**

**被static修饰的成员变量属于静态成员变量，存放在Java内存区域中的方法区。**

注意：静态方法访问本类的成员变量或成员方法时，也只能访问静态的成员变量和成员方法

> 方法区里面存的是已加载的类信息，常量，静态变量以及即时编译编译出的代码

* static静态内部类

静态内部类和非静态内部类的区别是，非静态内部类在编译时会产生一个隐式的引用指向创建它的外围类，而静态内部类就没有。没有这个引用意味着：

1. 静态内部类不需要依赖外部类进行创建
2. 静态内部类不能使用外部类的非static变量和方法

* static静态代码块

**静态代码块在非静态代码块之前执行（静态代码块-非静态代码块-构造方法），不管该类创建多少对象，静态代码块只执行一次。**

**静态代码块对于定义在它之后的静态变量，可以赋值，但是不能访问。**

* 静态导包

##### 子类父类静态代码块、非静态代码块、构造函数执行顺序

静态-非静态-构造，先父后子

### this

用于引用类当前的实例

### super

用于子类访问父类的当前实例

### final

- final修饰类：该类无法被继承，这个类中的方法默认是final的不可以被重写
- final修饰方法：该方法不能被重写
- final修饰变量：
  * 修饰基本类型：常量
  * 修饰引用类型：**该引用不可以再指向其他对象。但可以有其他对象引用指向同一个对象。**

## Java中接口与抽象类的区别

1. 接口中声明的成员默认是static final成员（不管是基本数据类型还是引用类型），且必须初始化
2. 接口中声明的方法默认为public且不能有实现，可以有参数
3. 实现接口的类，必须实现接口中的所有方法，且不能降低接口中方法的运用域，即必须是public
4. 抽象类不需要实现接口的方法，抽象类可以实现接口，可以选择实现部分方法
5. 在JDK8之后添加了接口中的默认方法，加上default关键字，还添加了静态方法

## Java的多态有什么作用和好处

## Java中的重写与重载有什么区别

### 重写

* 重写发生在子类继承父类时
* 重写方法的权限要大于等于父类方法的权限
* 重写是参数列表必须和父类方法相同；返回类型可以与父类的返回类型不同，但是必须是父类返回类型的子类；
* 重写的方法不能抛出一个比父类方法更宽泛的异常。
* 权限问题，只有子类可以重写父类中的方法，如果子类不在同一个包下或者父类设置了private权限，则按照权限来定。
* final方法不能被重写
* static方法不能被重写
* 构造方法不能被重写

附，权限作用表

|             | 自己类内部 | 同一个包下的类 | 子类 | 其他包下得类 |
| ----------- | ---------- | -------------- | ---- | ------------ |
| public      | ✔️          | ✔️              | ✔️    | ✔️            |
| protected   | ✔️          | ✔️              | ✔️    | 达咩         |
| no modifier | ✔️          | ✔️              | 达咩 | 达咩         |
| private     | ✔️          | 达咩           | 达咩 | 达咩         |

附，异常层次表

![img](/Users/xiaogengen/Desktop/秋招/MyRoadMap/Java.assets/exception-hierarchy-20210809153839221.png)

#### 子类方法可以重写父类的同步方法（synchronized）吗？

如果父类中的某个方法使用了 synchronized关键字，而子类中也覆盖了这个方法，默认情况下子类中的这个方法并不是同步的，必须显示的在子类的这个方法中加上 synchronized关键字才可。

#### 构造方法需要同步吗？

构造方法每次由一个线程构造出一个新的对象，不涉及到同步化的问题



## 关于try-catch-finally的执行顺序与return

1. 不管有没有出现异常，finally中的代码都会执行
2. try/catch中的return如果有返回值，finally还是会正常运行，try/catch中return的值会先保存成局部变量，在finally执行完成之后再返回
3. finally中的return值会覆盖掉try中的return值。finally中最好不要有return，不然程序会提前退出不会返回try/catch中的return值
4. 在try语句中执行System.exit(0)程序会直接退出

> finally不管怎样都会执行，try、catch中的return如果有返回值，finally也会正常执行，不会在try、catch之后就停止，返回值会先存成临时变量之后再finally执行完之后再返回，但是如果finally里面已经return了则try、catch里的return就作废不会返回了

# Java内存空间

## 运行时数据区域

### 1. 程序计数器

* 字节码解释器通过改变程序计数器来依次读取指令，从而实现代码的流程控制
* 在多线程的情况下，程序计数器用于记录当前线程执行的位置

### 2. 栈

Java 虚拟机栈是由一个个栈帧组成，而每个栈帧中都拥有：**局部变量表**、操作数栈、动态链接、方法出口信息。

栈中最重要的区域为局部变量表，局部变量表中主要存放了在编译期可知的各种数据类型（八大基本类型）和对象引用。

### 3. 本地方法栈

与Java虚拟机栈类似，只不过是用于运行Java中的Naive方法。

### 4. 堆

**几乎所有的对象实例以及数组都在这里分配内存**

为什么说是“几乎所有对象”，是因为**从 JDK 1.7 开始已经默认开启逃逸分析，如果某些方法中的对象引用没有被返回或者未被外面使用（也就是未逃逸出去），那么对象可以直接在栈上分配内存。**

### 5. 方法区

主要存储已被**虚拟机加载的类信息**，**常量**，**静态变量**，即时编译器编译后的代码等数据。

字符串常量池和运行时常量池都存在于方法区中。

#### 为什么将方法区的存储移至元空间？

1. 整个永久代有一个 JVM 本身设置的固定大小上限，无法进行调整，而元空间使用的是直接内存，受本机可用内存的限制，虽然元空间仍旧可能溢出，但是比原来出现的几率会更小。
2. 方法区中需要存储已加载的类信息，如果是在JVM内存中会受制于JVM分配的内存，在元空间中可以有更多的内存空间，可以加载更多的类信息。
3. 因为HotSpot和JRockit的合并？JRokit中没有方法区

#### 运行时常量池

#### ⭐️字符串常量池

<img src="https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/20210812160133.png" alt="String-Pool-Java" style="zoom:150%;" />

> 这里补充一点，如果再new一个s4也是”Cat“，

String Pool in Java is a pool of Strings stored in Java Heap Memory. We know that String is a Special class in Java and we can create String objects using a new Operator as well as providing values in double-quotes.

##### 字符串常量池及intern的使用：

1. 我们使用String str1 = "CatLulu";双引号声明出来的String对象会直接存储在常量池中
2. 不用双引号我们可以使用intern方法，String str2 = str1.intern(); intern方法的作用是：**如果运行时常量池中已经包含了一个等于此String对象内容的字符串，则返回常量池中该字符串的引用。**如果没有，JDK1.7之前会在常量池中创建一个与此String内容相同的字符串，并返回它的引用。JDK1.7之后是在常量池中记录此字符串的引用，并返回该引用。（？？？）

##### 字符串的拼接

```java
String str1 = "str";
String str2 = "ing";

String str3 = "str" + "ing";//常量池中的对象
String str4 = str1 + str2; //在堆上创建的新的对象
String str5 = "string";//常量池中的对象
System.out.println(str3 == str4);//false
System.out.println(str3 == str5);//true
System.out.println(str4 == str5);//false
```

![字符串拼接](https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/20210812160140.png)

字符串拼接中的锁消除问题：

```java
public String concatString(String s1, String s2, String s3) {
    return s1 + s2 + s3;
}
```

**由于String是一个不可变的类，对字符串的连接操作总是通过生成新的String对象来进行。（所以上面代码第5行+号连接两个字符串对象会返回一个堆上的字符串对象）**

在JDK5之前，字符串的➕拼接会自动转化为StringBuffer对象的连续append操作。在JDK5之后会转化为StringBuilder对象的连续append操作。

在经过逃逸分析之后发现，所有用于拼接的字符串中间变量都不会跑到方法外部，也不会被其他线程访问到，所以这里的锁可以被消除掉。

虽然在JDK5之后StringBuilder本身就不是一个线程安全的类，但这只是一个例子，来展示锁消除的原理，同时能够改成一个非线程安全的类也代表锁消除的基本概念。

#### String s1 = new String("abc");这句话创建了几个字符串对象？

**将创建 1 或 2 个字符串。如果池中已存在字符串常量“abc”，则只会在堆空间创建一个字符串常量“abc”。如果池中没有字符串常量“abc”，那么它将首先在池中创建，然后在堆空间中创建，因此将创建总共 2 个字符串对象。**

```java
String s1 = new String("abc");// 堆内存的地址值
String s2 = "abc";
System.out.println(s1 == s2);// 输出 false,因为一个是堆内存，一个是常量池的内存，故两者是不同的。
System.out.println(s1.equals(s2));// 输出 true
```



## 垃圾回收机制

### 如何判断对象是否应该被回收？



#### ⭐️JVM永久代相关知识：永久代会发生垃圾回收吗？。。。

#### ⭐️Full GC。。。





# Java里面的数据结构

## HashMap

### 1. HashMap的特点

### 2. 存储结构

数组 + 链表 + 红黑树

<img src="https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/20210812160146.png" alt="存储结构" style="zoom:75%;" />

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

### ⭐️红黑树

#### 红黑树的四个特点：

1. 根节点是黑色节点
2. 叶子节点是黑色的**空节点**，不存储数据
3. 每两个红色节点不能相邻
4. 每个（不是非得红色）节点到它可达的叶子节点的路径上必须要有相同数量的黑色节点

#### 红黑树是完全平衡的吗？为什么？

不是完全平衡的，红黑树只是做到总体高度接近于log2n

#### 红黑树为什么是接近平衡的，如何证明高度接近于log2n?

#### ⭐️红黑树如何保持平衡？



#### 红黑树和平衡二叉树的区别？他俩之间的性能差异和取舍

我们前面提到 Treap、Splay Tree，绝大部分情况下，它们操作的效率都很高，但是也无法避免极端情况下时间复杂度的退化。尽管这种情况出现的概率不大，但是对于单次操作时间非常敏感的场景来说，它们并不适用。

AVL 树是一种高度平衡的二叉树，所以查找的效率非常高，但是，有利就有弊，AVL 树为了维持这种高度的平衡，就要付出更多的代价。每次插入、删除都要做调整，就比较复杂、耗时。所以，对于有频繁的插入、删除操作的数据集合，使用 AVL 树的代价就有点高了。

红黑树只是做到了近似平衡，并不是严格的平衡，所以在维护平衡的成本上，要比 AVL 树要低。

所以，红黑树的插入、删除、查找各种操作性能都比较稳定。对于工程应用来说，要面对各种异常情况，为了支撑这种工业级的应用，我们更倾向于这种性能稳定的平衡二叉查找树。



### 3. 基本操作

#### （1）确定哈希桶数组索引位置

```java
// 方法一，jdk1.8 & jdk1.7都有：
static final int hash(Object key) {
     int h;
     return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
// 方法二，jdk1.7有，jdk1.8没有这个方法，但是实现原理一样的：
static int indexFor(int h, int length) {
     return h & (length-1);  
}
```

**主要分为三步：**

1. 取Key的HashCode值：h = key.hashCode();
2. 高位参与运算：h ^ (h >> 16)
3. 取模运算：h & (length - 1);

高位异或低位解释：这么做可以在数组table的length比较小的时候，也能保证考虑到**高低Bit都参与到Hash的计算中**，同时不会有太大的开销。

取模运算的解释：**当length总是2的n次方时，h& (length-1)运算等价于对length取模**

#### （2）⭐️put操作的详细流程

粘一下源码，扩容操作其实是先进行元素的插入之后在扩容的：

```java
public V put(K key, V value) {
    // 对key的hashCode()做hash
    return putVal(hash(key), key, value, false, true);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // 步骤①：tab为空则创建
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // 步骤②：计算index，并对null做处理 
    if ((p = tab[i = (n - 1) & hash]) == null) 
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        // 步骤③：节点key存在，直接覆盖value
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 步骤④：判断该链为红黑树
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        // 步骤⑤：该链为链表
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key,value,null);
                     //链表长度大于8转换为红黑树进行处理
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st  
                        treeifyBin(tab, hash);
                    break;
                }
                 // key已经存在直接覆盖value
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k)))) 
                           break;
                p = e;
            }
        }

        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }

    ++modCount;
    // 步骤⑥：超过最大容量 就扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

#### ⭐️HashMap是线程安全的吗？什么情况下会出现环？

#### ⭐️ConcurrentHashMap

ConcurentHashMap完成对HashMap线程安全方面的升级。

* ConcurrentHashMap是有**Segment数组结构**和**HashEntry数组结构**组成，Segment数组结构为分段锁，HashEntry数组结构用于存储键值对
* Segment是一种**可重入锁ReentrantLock**；Segment数组是一个数组+链表的结构，一个Segment里面包含一个HashEntry数组
* ConcurrentHashMap键值不能为null

#### （3）扩容机制

元素在重新计算Hash之后，因为n变为了两倍，所以只需要判断新增的高位是0还是1就好，如果是0那么这个node还是映射到原来的位置，如果是1就是映射到原来的位置+原来的size的位置

![这里写图片描述](https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/20210812160153.jpeg)





# Java异常

异常主要分为三类：

1. 检查性异常：这种异常通常是由于一些显式的错误产生，比如少写了;，或者输入的文件不存在，这种异常在编译时不会被忽略。
2. 运行时异常：运行时异常通常会被编译器忽略（不知道有什么例子）
3. 错误：错误不是异常，是程序员避免不了的（编码层面），比如堆栈溢出，属于错误不是异常。在编译时不能被发现。

### 1. Exception类：

![img](https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/20210812160155.png)

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

![截屏2021-08-05 上午12.05.49](https://raw.githubusercontent.com/Xiaogengenme/ImagesResource/main/20210812160203.png)

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



# Java中的锁










# Spring面试

## Spring framework的基本结构有哪些？

> 在面试中被问到Spring的基本结构或者说是Spring中最常被用到的包有哪些？

* Core Container：包括Core，Context和Beans等
* Data Access/Integration层，包括JDBC、ORM、Transaction等
* Web层，包括Web、Web-Servlet、WebSocket等
* AOP模块
* Test模块，包括JUnit等

## Spring Bean

### Spring中的Bean是什么？

bean 是一个被实例化，组装，并通过 Spring IoC 容器所管理的对象。

Spring对于Bean的管理包括两个方面：创建对象和注入属性

### Spring IoC的理解

控制反转：将原本在程序中手动创建对象的权利交由Spring框架来管理。

为实现IOC，Spring提供了一个容器用于创建和管理组件，也就是我们所说的Bean。

这些组件之间存在一些依赖关系，需要Spring容器对这些依赖组件进行装配，装配通过依赖注入（DI）的方式来实现。组件自己不会再去创建它所依赖的组件并管理它们的声明周期，而是都交给Spring容器来创建和维护所有的组件，然后在组件被需要的地方将其注入进去。

而组件之间的依赖关系可以由XML类型的描述文件或者Java @Configuration的配置类来定义，并由Spring来进行组件的装配。

当然这两种方式都是在Spring无法进行自动装配的时候才会被启用。

### Spring Bean

bean指的就是那些由IOC容器创建管理的组件。

与Spring IOC和Spring Bean相关的包主要有`org.springframework.beans`和`org.springframework.context`

#### Spring Bean的创建过程/生命周期⭐️

1. Bean容器找到Spring Bean 定义的地方，配置文件，可能是XML，也可能是Java配置类，当然这个是在自动配置不生效的情况下

2. Bean容器通过Java Reflection API创建一个Bean的对象实例

3. 使用set方法设置属性值

   > 怎么调用set方法设置？

4. 检查Bean实现的Aware接口，包括BeanNameAware接口、BeanClassLoaderAware接口、BeanClassLoaderAware接口、BeanFactoryAware接口，如果有就调用相关的set方法设置属性

   > Aware方法有什么用？

> 接下来更看不懂了，等下看一下视频

#### Spring Bean的作用域

- **singleton** : 唯一 bean 实例，Spring 中的 bean 默认都是单例的，对单例设计模式的应用。
- **prototype** : 每次请求都会创建一个新的 bean 实例。
- **request** : 每一次 HTTP 请求都会产生一个新的 bean，该 bean 仅在当前 HTTP request 内有效。
- **session** : 每一次 HTTP 请求都会产生一个新的 bean，该 bean 仅在当前 HTTP session 内有效。
- 还有一个**global-session**看不懂干嘛的不重要了

配置Bean的作用域：

1. XML方式

```xml
<bean id="..." class="..." scope="singleton"></bean>
```

2. Java配置类

```java
@Bean
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public Person personPrototype() {
    return new Person();
}
```

#### Spring Boot的启动流程







# 《Spring实战》

## 第一章 Spring起步

Spring提供了一个容器，称为Spring应用上下文，它们会创建和管理应用组件，这个组件就是Bean

Bean之间装配在一起是通过DI依赖注入来实现。

历史上我们我们指导Spring应用上下文来进行bean装配一般是使用一个或多个XML文件（描述各个组件以及它们之间的依赖关系）

```xml
<bean id="inventoryService"
      class="com.example.InventoryService"/>

<bean id="productService"
      class="com.example.ProductService" />
	<constructor-arg ref="inventoryService"/>
</bean>
```

而目前基于Java的配置类时更常见的方法：

```java
@Configuration
public class ServiceConfiguration {
    @Bean
    public InventoryService inventoryService() {
        return new InventoryService();
    }

    @Bean
    public ProductService productService() {
        return new ProductService(inventoryService());
    }
}
```

这个配置类方法使用@Bean注解，**表明这些方法所返回的对象会以bean的形式添加到Spring的IoC容器中**。

（通常情况下bean ID与定义他们的方法名称是相同的）

不论是Java还是XML的显式配置，只有当Spring不能进行自动装配时才是必要的。

Spring Boot自动装配原理之后学习。







## 那些关于组件的注解：配置类

### @Configuration

传统配置文件：xml文件，太麻烦不看了

现在使用@Configuration注解的配置类

```java
/**
 * 1、配置类里面使用@Bean标注在方法上类容器注册组件，默认是单例的
 * 2、配置类本身也是组件
 */
@Configuration(proxyBeanMethods = true)
public class Config {
    @Bean   // 给容器添加一个bean，以方法名作为名字
    public User user() {
        return new User("user01", "male");
    }

    @Bean
    public Pet pet() {
        return new Pet("yuanyuan", 2);
    }
}

```

关于proxyBeanMethods：

```java
// 获取spring ioc容器        
ConfigurableApplicationContext ioccontainer = SpringApplication.run(MyApplication.class, args);

// 获取bean
Pet pet = ioccontainer.getBean("pet", Pet.class);
Pet pet2 = ioccontainer.getBean("pet", Pet.class);
Config myConfig = ioccontainer.getBean(Config.class);
/**
	* proxyBeanMethods值为true时获取到的myConfig bean为：
	* com.bupt.boot.config.Config$$EnhancerBySpringCGLIB$$9b4af7a3@41d7b27f
	* 为代理对象
	* proxyBeanMethods值为false时获取到的myConfig bean为
	* com.bupt.boot.config.Config@58f174d9
*/
System.out.println(myConfig);
/**
	* 如果proxyBeanMethod值为true时这两个bean是相等的，==返回true
	* 默认创建的是单例的
*/
System.out.println(pet == pet2);

```

现在，当proxyBeanMethod = false时，也会采用单例模式，虽然返回的myConfig组件的类型与之前不一样，是com.bupt.boot.config.Config@4e2c95ee，不是代理类型，但是pet1和pet2的引用还是相等的，所以没什么必要把proxyBeanMethods设置成false了

* proxyBeanMethod = true解决组件依赖问题

```java
@Configuration(proxyBeanMethods = false)
public class Config {
    @Bean   // 给容器添加一个bean，以方法名作为名字
    public User user() {
        User xgg = new User("user01", "male");
      /**
      这里报错：Method annotated with @Bean is called directly in a @Configuration where proxyBeanMethods set to false. Set proxyBeanMethods to true or use dependency injection. 
      需要将proxyBeanMethods设置为true，每次引用一个组件bean
      */
        xgg.setPet(pet());
    }

    @Bean
    public Pet pet() {
        return new Pet("yuanyuan", 2);
    }
}

```

### @Bean、@Component、@Controller、@Service、@Repository

### @Import

```java
/**
添加@Import注解，会在容器中自动创建出这两个类型的组件，默认组件的名字是全类名
*/
@Import({User.class, DBHelper.class})
@Configuration(proxyBeanMethods = false)
public class Config {
```

### @Conditional

遇到bug：

```java
***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 0 of constructor in com.bupt.boot.bean.User required a bean of type 'java.lang.String' that could not be found.


Action:

Consider defining a bean of type 'java.lang.String' in your configuration.
```

解决办法：要在User类中加入无参构造函数。

@Conditional注解主要用于一定条件下的组件注入

```java
=====================测试条件装配==========================
@Configuration(proxyBeanMethods = false) //告诉SpringBoot这是一个配置类 == 配置文件
//@ConditionalOnBean(name = "tom")
  /*
  这里，只有在容器中不包括tom时才进行组件注入
  */
@ConditionalOnMissingBean(name = "tom")
public class MyConfig {
    @Bean //给容器中添加组件。以方法名作为组件的id。返回类型就是组件类型。返回的值，就是组件在容器中的实例
    public User user01(){
        User zhangsan = new User("zhangsan", 18);
        //user组件依赖了Pet组件
        zhangsan.setPet(tomcatPet());
        return zhangsan;
    }

    @Bean("tom22")
    public Pet tomcatPet(){
        return new Pet("tomcat");
    }
}

public static void main(String[] args) {
        //1、返回我们IOC容器
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

        //2、查看容器里面的组件
        String[] names = run.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }

        boolean tom = run.containsBean("tom");
        System.out.println("容器中Tom组件："+tom);

        boolean user01 = run.containsBean("user01");
        System.out.println("容器中user01组件："+user01);

        boolean tom22 = run.containsBean("tom22");
        System.out.println("容器中tom22组件："+tom22);
}
```

### @ImportResource

原生配置文件导入，为一个配置类引入老式的xml配置文件

### 配置绑定：@ConfigurationProperties

```java
@Data
@Component
@ConfigurationProperties(prefix = "mycar")
public class Car {
    private String brand;
    private Integer price;

    public Car() {

    }
    
    public Car(String brand, Integer price) {
        this.brand = brand;
        this.price = price;
    }
}
```

为这个Car类标注了@ConfigurationProperties注解之后可以将Car中的属性与配置文件中以mycar为前缀的属性相关联

```java
// application.properties
mycar.brand = Benz
mycar.price = 10000000000
```

**但需要注意的是想要使用@ConfigurationProperties注解的类必须把自己注册成一个组件@Component**

也可以在配置类中标明某个类可以使用@ConfigurationProperties，这样配置类会自动将其注入到容器中成为一个组件bean

```java
@EnableConfigurationProperties(Car.class)
public class Config {
```



## Spring Boot的自动配置原理

@SpringBootApplication = @SpringBootConfiguration + @ConponentScan + @EnableAutoConfiguration

1. @SpringBootConfiguration：底层是@Configuration，代表也是一种配置类

2. @ConponentScan：组件扫描

3. @EnableAutoConfiguration

   ```java
   @AutoConfigurationPackage
   @Import({AutoConfigurationImportSelector.class})
   ```

   1. @AutoConfigurationPackage:

> 自动配置以后再看吧。。。。



# Web开发

### 1. 配置文件

yaml配置文件编写方式：

* key: value
* 大小写敏感
* 使用缩紧表示层级关系
* 缩进的空格数不重要，只要相同层级的元素左对齐即可

例子：

Person类：

```java
@Data
public class Person {
    private String userName;
    private Boolean boss;
    private Date birth;
    private Integer age;
    private Pet pet;
    private String[] interests;
    private List<String> animal;
    private Map<String, Object> score;
    private Set<Double> salarys;
    private Map<String, List<Pet>> allPets;
}
```

对应yaml配置：

中括号表示List，也可以用 - item

大括号表示Map

关于对象直接设置对象的属性值（看pet）

```yaml
person:
  name: xiaogengen
  gender: male
  birth: 1997/5/7
  age: 22
  # interests: [游泳, 潜泳, 排球]
  interests:
    - 篮球
    - 排球
    - 曲棍球
    - 象棋

  animal:[猫猫, 狗沟]
  salarys:
    # private Set<Double> salarys;
    - 9999
    - 9999
  pet:
    # private Pet pet;
    # public class Pet {
    #    private String name;
    #    private int age;
    name: 大柱
    age: 3
  score:
    # private Map<String, Object> score;
    {english: 80, math: 90}
  allPets:
    sick:
      - { name: tom }
      - { name: jerry,weight: 47 }
    health: [ { name: mario,weight: 47 } ]
```



### 2. 静态资源访问

1. 静态资源目录：

   只要静态资源放在以下项目路径下： `/static` (or `/public` or `/resources` or `/META-INF/resources`）

   我们访问 当前项目根路径 + / + 静态资源名，就可以访问到

   原理：请求会首先寻找可以处理请求路径的controller，没有的话再找静态资源（路径/ + 静态资源名/**），再没有就404

   * 在配置文件中修改静态资源路径：

   ```yaml
   spring:
     mvc:
       static-path-pattern: /res/**
   ```

   代表所有要访问静态资源的请求前要加“/res/静态资源名”(localhost:8080/res/pic.png)路径

   * 在配置文件中设置静态资源的存储路径

   ```yaml
   resources:
       static-locations: [classpath:/haha/]
   ```

   这样设置之后，只有放置在/resouces/haha/文件夹下的静态文件才能够被获取到

#### Rest风格请求参数处理

##### 1. 请求映射

请求映射就是指在Contoller中的方法上标注方法可以处理的路径

请求映射如果把操作都写到path上：/getUser, /deleteUser, /editUser, /saveUser关于用户的操作有很多各种请求

Rest风格的请求映射：使用HTTP请求方式的几个动词表示对资源的操作：

比如对user的操作：GET, DELETE, PUT, POST



### 3. Controller编写

#### 参数问题：几种参数设置方法和常用注解

#### （1）普通注解方式

##### @PathVariable：路径参数

将请求中的路径作为参数

```java
@GetMapping("/car/{id}/owner/{username}")
    public Map getCarInfo(@PathVariable("id") Integer id
            ,@PathVariable("username") String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        return map;
    }
```

可以使用@PathVariable注解将路径参数转化为方法的参数，在写映射语句时在对应参数上加{}

可以使用@PathVariable Map<String, String> pathVariableMap作为参数，这个map包含所有路径参数的kv值

```java
@GetMapping("/car/{id}/owner/{username}")
    public Map getCarInfo(@PathVariable("id") Integer id,
                          @PathVariable("username") String name,
                          @PathVariable Map<String, String> pathVariableMap) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("pathVariableMap", pathVariableMap);
        return map;
    }
```



##### @RequestHeader：获取请求头内容

可以在controller的方法中使用@RequestHeader注解获取本次请求的请求头中对应的request-header的内容；

同样也可以使用一个@RequestHeader Map<String, String> headerMap来将请求头的kv存储在一个map中。

```java
@RestController
public class TestParameterController {
    @GetMapping("/car/{id}/owner/{username}")
    public Map getCarInfo(@PathVariable("id") Integer id,
                             @PathVariable("username") String name,
                             @RequestHeader("connection") String connection,
                             @RequestHeader Map<String, String> requestHeaders,
                             @PathVariable Map<String, String> pv) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("request-header:connection", connection);
        map.put("all-request-headers", requestHeaders);
        map.put("pv", pv);
        return map;
    }
```



##### @CookieValue：获取cookie中的内容



#### （2）使用HttpServletRequest传递

```java
@Controller
public class RequestController {

  // 这里使用HttpServletRequest作为参数传递，并使用请求跳转跳转到另一个地址
    @GetMapping("/page1")
    public String goPage1(HttpServletRequest request) {
        request.setAttribute("msg", "现在要跳转到页面2");
        request.setAttribute("code", "200(假）");
        return "forward:/page2";
    }

  // 这里接收上一个请求传递进来的HttpServletRequest，并使用一个新的httpServletRequest来接收
  // 并使用@RequestAttribute注解来对httpServletRequest中的属性进行接收
    @ResponseBody
    @GetMapping("/page2")
    public Map getPage2(@RequestAttribute("msg") String msg,
                        @RequestAttribute("code") String code,
                        HttpServletRequest httpServletRequest) {
        Map<String, Object> map = new HashMap<>();
        Object msg1 = httpServletRequest.getAttribute("msg");
      // 两种方式：一个是在参数中使用@RequestAttribute注解
      // 另一种是使用httpServletRequest.getAttribute方法
        map.put("msg: get from atrribute:", msg);
        map.put("msg1: get from httpServletRequest:", msg1);
        map.put("code", code);
        //map.put("HttpServletRequest", httpServletRequest);
        System.out.println(map);
        return map;
    }
}
```







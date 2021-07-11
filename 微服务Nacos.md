## nacos作为配置中心

当我们很多的服务都部署在服务器上时，为每个服务编写配置文件是一件麻烦的事情

我们需要一个配置中心来统一管理配置，每个单独的服务从配置中心拉取配置文件

nacos的一个重要的职责就是作为一个配置中心

<img src="/Users/lizhigen/Library/Application Support/typora-user-images/截屏2021-07-08 下午1.45.07.png" alt="截屏2021-07-08 下午1.45.07" style="zoom:30%;" />

cd /root//nacos/bin

sh startup.sh -m standalone

cd /root/nacos/distribution/target/nacos-server-2.0.3-SNAPSHOT/nacos/logs/

sh start.out
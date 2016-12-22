# memcloud

## how to build & run & test

	mvn clean package -Dmaven.test.skip=true -Pqa
	mvn jetty:run -Djetty.port=10008
	curl -x 10.213.57.166:10008 "http://passport.ffan.net/passport/session-create.json?loginid=chinaliwee@163.com&pwd=123456" -i
	curl -x 10.213.57.166:10008 "http://passport.ffan.net/passport/account-lookup.json?type=map&uids=2,1ueIUbXoMNQ." -i

# 术语

- **mem-instance**: memcached的一个进程。
- **mem-sharding**: 又名 **mem-peers** 或 **mem-partition** 。表示两个互相备份的mem-instance构成的集合。
- **appid**: 对接入memcached缓存服务的应用方的唯一标识。
- **mem-cluster**: 隶属于同一个``appid``的一个或多个``mem-peers``的集合。
- **mem-cloud**: 所有``mem-cluster``构成的集合。
- **mem-dns**: 又名 **meta-server**。 存储所有从``appid``到``mem-cluster``的映射关系的服务。起名**mem-dns**，是因为它对类似从域名到多个IP的映射。
- **mem-host**: 部署``mem-instance``的主机。一个``mem-host``往往有多个``mem-instance``（当然目前一个``mem-instance``只能在一个``mem-host``）。
- **allocated mem-instance**: 被分配给具体``appid``使用的``mem-instance``。
- **unallocated mem-instance**: 尚未分配给具体``appid``使用的``mem-instance``。
- **mem-instance pool**: 所有``unallocated mem-instance``构成的集合。
- **Assignment strategy**: 从``mem-instance pool``中选择部分``unallocated mem-instance``给具体的``appid``的过程。
- **Replica strategy**: 隶属同一个``mem-peers``的两个``mem-instance``相互备份数据的算法。
- **Partition strategy**: 一个``appid``分配多少个``mem-peers``，依据什么分配其中一个``mem-peers``，以及在客户端还是服务端实现的决策过程的总称。
- **mem-instance discovery**: 又名 **mem-instance register**，表示一个新的``mem-instance``加入到``mem-dns``的机制和过程（也是加入``mem-instance pool``的过程）。
- **mem-agent**: 负责安装并启动``mem-instance``的软件集合。
- **mem-client**: 提供给某个``appid``应用来访问``mem-cloud``的客户端程序。
- **appid isolation strategy**: 不同``appid``共享``mem-cloud``的隔离机制，以便一方面共享，另一方面彼此不相互干扰。

# 附录-1：分布式存储基本问题

>分布式存储系统都需要回答三个基本问题：Partition，Replica，和Assignment。
- **Partition**:  数据量大了的时候，如何切分成多个小块。所有小块合并起来是整个大块，每个小块之间没有数据交叉。完全符合集合论中的Partition定义。Partition的切分算法，在服务端还是客户端实现等。
- **Replica**: 为防止一份数据丢失（比如磁盘坏掉），数据需要冗余多份。多份的策略是什么？两个磁盘，两个机器，两个机房，异地机房？写副本是同步，还是异步（所谓同步是写Master的时候，要等写完副本时，才返回；异步则是写Master的时候，先返回，然后系统稍后再写副本）。
- **Assignment**: 又名**orchestration**（服务编排），前面**Partition**和**Replica**都是逻辑层面的概念（完全是数学模型层面的东西），不涉及到具体的机器部署。涉及到机器部署层面的，假如有个分布系统有2个partition，2个replica，用的是Master-Slave形式，给2个机器，那么部署至少有两种可能：
 * 同类部署：HostA作为Master，P0.Master和P1.Master都部署到HostA上；Slave都部署到HostB上。
 * 交叉部署：HostA部署P0.Master和P1.Slave；而HostB部署P1.Master和P0.Slave。

>两种部署哪种好呢？交叉部署更好一点，因为通常写操作只能在Master上，交叉部署时，有助于分散写操作到两个主机，如果同类部署，则会导致HostA很忙，HostB很闲。真的是这样吗？如果HostA是一台高性能机器，HostB只是一台普通机器呢？可能部署策略又反过来了。

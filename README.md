# Campusecurity 校园智能安防系统
![img](./img/title.png)

本项目为基于分布式微服务框架Spring Cloud的物联网项目。
旨在兼容各种物联网设备的存储与通信标准，实现校园安防系统设备互联、管理、数据采集、热部署、拓展开发等。
![img](./img/2.png)
## 总体结构
![img](./img/1.png)

*各种前后端交互接口与服务集群内部接口详见feign-api包

## idea中各服务配置与启动方式
按照顺序启动：

    数据库: 
        mysql jdbc:mysql://localhost:3306/campusecurity?serverTimezone=GMT
        redis  localhost:6379
    注册发现中心 eureka  localhost:8761/eureka
    配置中心 configserver localhost:8888
    用户服务 userservice http://localhost:8762 或  lb://userservice
    设备管理服务 devicemanage http://localhost:8763 或 lb://devicemanage
    网关兼前端 geteway http://localhost:8889
    设备通信服务:
        NVR(摄像头的上位机)适配与通信服务 nvrlistener http://localhost:8764 
        烟雾报警器(独立硬件)适配与通信服务 tcpadapter http://localhost:8765

## 数据库结构
![img](./img/3.png)

## 前后端分离方案
为了减少开发成本，前端（gateway包）基于thymeleaf模板与spring mvc，非静态网页，
而且添加了过滤器进行鉴权等操作，不算严格意义上的前后端分离方案，但显然与业务逻辑层隔离，之间使用http协议通信。

## docker问题
创建两个分支，一个是docker容器版本master，一个是非docker的开发环境版本
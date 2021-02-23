package cn.macswelle.campusecurity.devicemanage.service;

/**
 * 设备管理服务需要实现的功能：
 *  从注册中心读取硬件服务内容与性质
 *  在页面展示各硬件的管理页面
 *  接受接口监听服务与设备通信服务通过消息总线传来的数据，写入数据库，包括何人何时出现在何地
 */
public interface DeviceManageService {

    void getServices();


}

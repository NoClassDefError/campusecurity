package cn.macswelle.campusecurity.common;

/**
 * 设备通信服务必须要继承这个类来描述自己的信息。
 */
public abstract class Adapter {

    public abstract String getName();//例如 摄像头（型号ASDF_38219）的适配器

    public abstract String getLocation();//适配器地点

    public abstract String getDescription();//描述

    public abstract String getVersion();//适配器版本

    public abstract Integer getCategory();

}

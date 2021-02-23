package cn.macswelle.campusecurity.nvrlistener;

import cn.macswelle.campusecurity.common.Adapter;
import cn.macswelle.campusecurity.common.entities.Device;

import java.util.List;

public final class Info extends Adapter {

    @Override
    public String getName() {
        return "NVR GB31415";
    }

    @Override
    public String getUrl() {
        return "http://10.16.92.15:8888";
    }

    @Override
    public String getLocation() {
        return "10#寝室六楼东";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public Integer getCategory() {
        return 4;//"rtmp协议"
    }

    @Override
    public List<Device> getDevices() {
        return null;
    }
}

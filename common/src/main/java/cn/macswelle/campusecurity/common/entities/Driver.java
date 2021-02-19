package cn.macswelle.campusecurity.common.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 驱动类，也可以理解成设备种类，因为默认同类设备使用同种驱动。
 * 硬件按照是否有独立数据库，分为间接接入与直接接入两类，其中直接接入的硬件又分为支持与不支持http协议两类。
 * 支持http协议的硬件作为客户端纳入系统，不支持http协议的由设备通信服务管理。
 *
 * @author Gehanchen
 */
@Data
@Entity
public class Driver {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "name")
    private String name;

    /**
     * 间接接入 0
     * http协议 1
     * mqtt协议 2
     * modbus-tcp协议 3
     * mqtt 协议 4
     * opc-da 协议 5
     * opc-ua 协议 6
     * plcs7 协议 7
     */
    @Basic
    @Column(name = "category")
    private Integer category;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "version")
    private String version;

    @OneToMany(targetEntity = Device.class, mappedBy = "driver", cascade = CascadeType.DETACH)
    private List<Device> devices;
}

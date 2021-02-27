package cn.macswelle.campusecurity.common.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 单一设备，例如两个一样的摄像头，Device的id不同
 */
@Entity
@Data
public class Device {
    @Id
    @Basic
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne(targetEntity = Location.class)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;
}

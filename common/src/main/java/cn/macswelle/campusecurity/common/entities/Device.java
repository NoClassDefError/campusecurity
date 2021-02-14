package cn.macswelle.campusecurity.common.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
public class Device {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "name")
    private String name;

    /**
     * 驱动种类 1 直接消息订阅 2  3 4 5
     */
    @Basic
    @Column(name = "category")
    private Integer category;

    @ManyToOne(targetEntity = Location.class)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;
}

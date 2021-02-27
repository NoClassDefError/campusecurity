package cn.macswelle.campusecurity.common.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * 包括人员与车辆
 */
@Data
@Entity
public class Personnel {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "name")
    private String name;

    /**
     * 1 人员 2 车辆 3 其他 默认为1
     */
    @Basic
    @Column(name = "category", columnDefinition = "integer default 1", nullable = false)
    private Integer category = 1;

    @Basic
    @Column(name = "description")
    private String description;

    @OneToMany(targetEntity = Record.class, mappedBy = "personnel", cascade = CascadeType.DETACH)
    private List<Record> places;
}

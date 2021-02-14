package cn.macswelle.campusecurity.common.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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
     * 1 人员 2 车辆 默认为1
     */
    @Basic
    @Column(name = "category", columnDefinition = "integer default 1", nullable = false)
    private Integer category = 1;

    @OneToMany(targetEntity = Record.class, mappedBy = "personnel", cascade = CascadeType.DETACH)
    private List<Record> places;
}

package cn.macswelle.campusecurity.common.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "name")
    private String name;

    @OneToMany(targetEntity = Record.class, mappedBy = "location", cascade = CascadeType.DETACH)
    private List<Record> personnel;

    @OneToMany(targetEntity = Device.class, mappedBy = "location", cascade = CascadeType.DETACH)
    private List<Device> devices;
}

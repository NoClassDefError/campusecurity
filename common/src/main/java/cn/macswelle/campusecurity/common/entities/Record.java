package cn.macswelle.campusecurity.common.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

@Data
@Entity
public class Record {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private Long createTime;

    @ManyToOne(targetEntity = Location.class)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;

    @ManyToOne(targetEntity = Personnel.class)
    @JoinColumn(name = "personnel", referencedColumnName = "id")
    private Personnel personnel;
}

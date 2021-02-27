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

    /**
     * 代表事件种类，除了人或车出现在特定位置，还有火灾预警等场景
     */
    @Basic
    @Column(name = "event")
    private String event;

    /**
     * 如果这个事件有文件记录的话，记录文件的url
     */
    @Basic
    @Column(name = "file")
    private String file;

    @ManyToOne(targetEntity = Location.class)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;

    /**
     * 不是所有设备都能检测到是什么人，这个字段可能为空
     * 而且理论上数据库外键可以为空
     */
    @ManyToOne(targetEntity = Personnel.class)
    @JoinColumn(name = "personnel", referencedColumnName = "id")
    private Personnel personnel;
}

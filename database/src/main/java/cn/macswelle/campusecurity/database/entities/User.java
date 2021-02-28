package cn.macswelle.campusecurity.database.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "name", unique = true)
    private String name;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "description")
    private String description;

    /**
     * 0 超级管理员，有所有权限
     * 1 普通管理员，没有账号注册权限
     * 2 只读
     */
    @Basic
    @Column(name = "auth", nullable = false, columnDefinition = "integer default 1")
    private Integer auth;
}

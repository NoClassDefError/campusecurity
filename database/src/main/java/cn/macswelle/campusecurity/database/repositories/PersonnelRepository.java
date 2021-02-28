package cn.macswelle.campusecurity.database.repositories;

import cn.macswelle.campusecurity.database.entities.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonnelRepository extends JpaRepository<Personnel,String>,
        JpaSpecificationExecutor<Personnel> {

}

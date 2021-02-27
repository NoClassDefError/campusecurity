package cn.macswelle.campusecurity.devicemanage.repositories;

import cn.macswelle.campusecurity.common.entities.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonnelRepository extends JpaRepository<Personnel,String>,
        JpaSpecificationExecutor<Personnel> {

}

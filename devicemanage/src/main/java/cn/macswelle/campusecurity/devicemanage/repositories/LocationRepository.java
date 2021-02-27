package cn.macswelle.campusecurity.devicemanage.repositories;

import cn.macswelle.campusecurity.common.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,String>,
        JpaSpecificationExecutor<Location> {

}

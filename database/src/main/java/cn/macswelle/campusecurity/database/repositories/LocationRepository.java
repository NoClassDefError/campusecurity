package cn.macswelle.campusecurity.database.repositories;

import cn.macswelle.campusecurity.database.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,String>,
        JpaSpecificationExecutor<Location> {

  Location findByName(String name);
}

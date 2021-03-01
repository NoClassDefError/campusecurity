package cn.macswelle.campusecurity.database.repositories;

import cn.macswelle.campusecurity.database.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String>,
        JpaSpecificationExecutor<Device> {
    @Query("select l from Device l where l.location.id = ?1")
    List<Device> findByLocation(String id);

    @Query("select count(d) from Device d where d.location.id = ?1")
    int findNumByLocation(String id);
}

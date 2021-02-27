package cn.macswelle.campusecurity.devicemanage.repositories;

import cn.macswelle.campusecurity.common.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String>,
        JpaSpecificationExecutor<Device> {

}

package cn.macswelle.campusecurity.database.repositories;

import cn.macswelle.campusecurity.database.entities.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, String>,
        JpaSpecificationExecutor<Record> {
    @Query("select l from Record l where l.location.id = ?1 and l.createTime>?2 and l.createTime<?3")
    List<Record> findByLocation(String id, Long start, Long end);

    @Query("select count(d) from Record d where d.location.id = ?1")
    int countByLocation(String id);
}

package cn.macswelle.campusecurity.userservice.repositories;

import cn.macswelle.campusecurity.common.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByNameAndPassword(String name, String password);

    @Query("select user.auth from User user where user.id=?1")
    Integer findUserAuthById(String id);

}

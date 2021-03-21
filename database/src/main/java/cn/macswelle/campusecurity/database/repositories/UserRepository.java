package cn.macswelle.campusecurity.database.repositories;

import cn.macswelle.campusecurity.database.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  List<User> findByIdAndPassword(String id, String password);

  @Query("select user.auth from User user where user.id=?1")
  Integer findUserAuthById(String id);

  @Transactional
  @Modifying
  @Query("update User user set user.name=?2, user.auth=?4, user.description=?3 where user.id=?1")
  void updateUser(String id, String name, String description, Integer auth);
}

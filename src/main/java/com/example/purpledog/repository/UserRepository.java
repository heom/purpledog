package com.example.purpledog.repository;

import com.example.purpledog.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    //findById()
        // select * from users where id=?
    //existsById()
        // select count(*) from users where id=?
    //save()
        // insert into users(created_date, last_modified_date, password, id) values (?, ?, ?, ?)
        // update users set last_modified_date=?, password=? where id=?
    //delete()
        // delete from users where id=?
}

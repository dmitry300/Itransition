package com.itransition.site.repos;

import com.itransition.site.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    User findByActivationCode(String code);
}

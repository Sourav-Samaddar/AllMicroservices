package com.lcwd.user.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.user.service.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

}

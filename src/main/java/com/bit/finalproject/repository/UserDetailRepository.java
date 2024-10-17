package com.bit.finalproject.repository;


import com.bit.finalproject.entity.UserDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long > {

}

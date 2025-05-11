package com.example.sews.repo;


import com.example.sews.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u.deviceId from User u where u.userId=:userId")
    String getDeviceIDByUserId(@Param("userId") Integer userId);
    // 更新设备ID字符串
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.deviceId = :deviceId WHERE u.userId = :userId")
    int  updateDeviceID(@Param("userId") Integer userId, @Param("deviceId")String deviceId);


    List<User> findByAdminId(Integer adminId);
}

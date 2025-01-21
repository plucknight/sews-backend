package com.example.sews.repo;

import com.example.sews.dto.Trap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrapRepository extends JpaRepository<Trap, Integer> {

    // 根据设备ID查询诱捕器
    List<Trap> findByDeviceId(Integer deviceId);
}

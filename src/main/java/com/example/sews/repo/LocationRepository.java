package com.example.sews.repo;

import com.example.sews.dto.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author: myh
 * @CreateTime: 2025-04-01  16:54
 * @Description: TODO
 * @Version: 1.0
 */
@Repository
public interface LocationRepository extends JpaRepository<Location,Integer> {
    // 根据县区查找 Location
    Location findByCounty(String county);

    Optional<Location> findByProvinceAndCityAndCounty(String province, String city, String county);

    // 根据城市和县区查找 Location（如果你希望更加精确，可以同时查询 city 和 county）
    Location findByCityAndCounty(String city, String county);
}

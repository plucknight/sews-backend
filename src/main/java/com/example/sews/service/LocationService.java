package com.example.sews.service;

import com.example.sews.dto.Location;
import com.example.sews.repo.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: myh
 * @CreateTime: 2025-04-28  20:51
 * @Description: TODO
 * @Version: 1.0
 */

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;
    public List<Location> findAll(){
        return locationRepository.findAll();
    }
}

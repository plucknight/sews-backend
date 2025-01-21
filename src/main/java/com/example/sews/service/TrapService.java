package com.example.sews.service;

import com.example.sews.dto.Trap;
import com.example.sews.repo.TrapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrapService {

    @Autowired
    private TrapRepository trapRepository;

    /**
     * 添加诱捕器
     * @param trap 诱捕器对象
     * @return 创建的诱捕器
     */
    public Trap addTrap(Trap trap) {
        // 保存诱捕器到数据库
        return trapRepository.save(trap);
    }

    /**
     * 查询所有诱捕器
     * @return 诱捕器列表
     */
    public List<Trap> getAllTraps() {
        // 返回所有诱捕器信息
        return trapRepository.findAll();
    }

    /**
     * 根据设备ID查询诱捕器
     * @param deviceId 设备ID
     * @return 设备对应的诱捕器列表
     */
    public List<Trap> getTrapsByDeviceId(int deviceId) {
        // 查询并返回该设备下的所有诱捕器
        return trapRepository.findByDeviceId(deviceId);
    }

    /**
     * 根据诱捕器ID查询诱捕器
     * @param trapId 诱捕器ID
     * @return 对应的诱捕器信息
     */
    public Trap getTrapById(int trapId) {
        // 查找并返回诱捕器，如果不存在则返回null
        Optional<Trap> trap = trapRepository.findById(trapId);
        return trap.orElse(null);
    }
}

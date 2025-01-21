package com.example.sews.controller;

import com.example.sews.dto.Trap;
import com.example.sews.service.TrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traps")
public class TrapController {

    @Autowired
    private TrapService trapService;

    // 添加诱捕器
    @PostMapping("/addTrap")
    public ResponseEntity<Trap> addTrap(@RequestBody Trap trap) {
        Trap createdTrap = trapService.addTrap(trap);
        return ResponseEntity.status(201).body(createdTrap);
    }

    // 查询所有诱捕器
    @GetMapping("/getAllTraps")
    public ResponseEntity<List<Trap>> getAllTraps() {
        List<Trap> traps = trapService.getAllTraps();
        return ResponseEntity.ok(traps);
    }

    // 根据设备ID查询诱捕器
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<Trap>> getTrapsByDeviceId(@PathVariable("deviceId") int deviceId) {
        List<Trap> traps = trapService.getTrapsByDeviceId(deviceId);
        return ResponseEntity.ok(traps);
    }

    // 根据诱捕器ID查询诱捕器
    @GetMapping("/{trapId}")
    public ResponseEntity<Trap> getTrapById(@PathVariable("trapId") int trapId) {
        Trap trap = trapService.getTrapById(trapId);
        return trap != null ? ResponseEntity.ok(trap) : ResponseEntity.notFound().build();
    }
}

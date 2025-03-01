package com.example.sews.service;

import com.example.sews.dto.vo.AdminPermissionDto;
import com.example.sews.repo.AdminModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminModelService {

    @Autowired
    private AdminModelRepository adminModelRepository;

    // 获取管理员和模型的联合信息
    public List<AdminPermissionDto> getAdminModelInfo() {
        return adminModelRepository.findAdminModelInfo();
    }
}

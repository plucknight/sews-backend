package com.example.sews.repo;

import com.example.sews.dto.AdminModel;
import com.example.sews.dto.vo.AdminModelInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminModelRepository extends JpaRepository<AdminModel, Long> {

    // 自定义查询：联合查询 admin、model_info 和 admin_model 表，返回管理员和模型的主要信息
    @Query("SELECT new com.example.sews.dto.vo.AdminModelInfoDTO(a.username, a.email, m.modelName, m.modelType, m.modelVersion) " +
            "FROM AdminModel am " +
            "JOIN Admin a ON a.id = am.adminId " +    // 联接 Admin 表
            "JOIN ModelInfo m ON m.modelId = am.id")  // 联接 ModelInfo 表
    List<AdminModelInfoDTO> findAdminModelInfo();
}

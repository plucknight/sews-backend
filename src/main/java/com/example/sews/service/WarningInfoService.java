package com.example.sews.service;

import com.example.sews.dto.WarningInfo;
import com.example.sews.repo.WarningInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningInfoService {

    @Autowired
    private WarningInfoRepository warningInfoRepository;

    /**
     * 创建预警信息
     * @param warningInfo 预警信息对象
     * @return 返回是否创建成功
     */
    public boolean createWarningInfo(WarningInfo warningInfo) {
        try {
            warningInfoRepository.save(warningInfo);  // 保存到数据库
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据设备ID获取相关预警信息
     * @param deviceId 设备ID
     * @return 返回该设备的所有预警信息列表
     */
    public List<WarningInfo> getWarningInfoByDevice(Integer deviceId) {
        return warningInfoRepository.findByDeviceId(deviceId);  // 查询设备ID对应的预警信息
    }

    /**
     * 更新预警信息
     * @param warningId 预警信息ID
     * @param warningInfo 更新后的预警信息
     * @return 是否更新成功
     */
    public boolean updateWarningInfo(Integer warningId, WarningInfo warningInfo) {
        try {
            WarningInfo existingWarning = warningInfoRepository.findById(warningId).orElse(null);
            if (existingWarning != null) {
                existingWarning.setWarningLevel(warningInfo.getWarningLevel());
                existingWarning.setWarningMessage(warningInfo.getWarningMessage());
                existingWarning.setPublishDate(warningInfo.getPublishDate());
                warningInfoRepository.save(existingWarning);  // 更新数据库
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除预警信息
     * @param warningId 预警信息ID
     * @return 是否删除成功
     */
    public boolean deleteWarningInfo(Integer warningId) {
        try {
            warningInfoRepository.deleteById(warningId);  // 删除数据库中的预警信息
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<WarningInfo> getAllWarningInfo() {
        return warningInfoRepository.findAll();
    }
}

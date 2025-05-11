package com.example.sews.service.aspect;

import com.example.sews.dto.OperateLog;
import com.example.sews.dto.anno.LogOperation;
import com.example.sews.repo.OperateLogRepository;
import com.example.sews.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class OperateLogAspect {

    @Autowired
    private OperateLogRepository operateLogRepository; // 注入你的日志保存接口

    @Pointcut("@annotation(com.example.sews.dto.anno.LogOperation)") // 改成你自己的包路径
    public void logPointcut() {}
    @Autowired
    private HttpServletRequest request;
    @AfterReturning(pointcut = "logPointcut()", returning = "result")
    public void saveLog(JoinPoint joinPoint, Object result) {
        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogOperation logOperation = method.getAnnotation(LogOperation.class);

        // 获取操作描述
        String action = logOperation.value();
// 从请求头中获取 token
        String token = request.getHeader("Authorization");
        Integer adminId = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            adminId = Integer.parseInt(claims.getSubject()); // subject 存的是 adminId
        } catch (Exception e) {
            // 处理异常或记录日志
            return;
        }
        // 获取请求参数（可选）
        Object[] args = joinPoint.getArgs();
        String description = Arrays.toString(args); // 建议根据需要自定义描述内容

        // 构造 OperateLog 实体
        OperateLog log = new OperateLog();
        log.setAdminId(adminId);
        log.setAction(action);
        log.setDescription(description);
        log.setCreatedAt(LocalDateTime.now());

        // 保存日志
        operateLogRepository.save(log);
    }
}

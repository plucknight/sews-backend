package com.example.sews.security;

import com.example.sews.dto.Admin;
import com.example.sews.dto.LoginUser;
import com.example.sews.service.AdminService;
import com.example.sews.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AdminService adminService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

//        //获取token
        String token = request.getHeader("token");
        // 获取请求的URL
        String requestURL = request.getRequestURL().toString();
        Pattern pattern = Pattern.compile("://[^/]+:(\\d+)/(.*)");
        Matcher matcher = pattern.matcher(requestURL);
        String path = "";
        if (matcher.find()) {
            path = matcher.group(2); // 端口号后面的部分
            System.out.println(" Path: " + path);
        }
//        System.out.println("requestURL = " + requestURL);
        // 判断URL是否包含/login或/map
        if ("admin/login".equals(path) || "map".equals(path)|| "prediction-results/getAll".equals(path)) {
            // 如果包含，则直接执行chain.doFilter
            chain.doFilter(request, response);
            return ;
        }
//        //解析token
        String adminId = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            adminId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        Optional<Admin> admin =  adminService.findById(Long.parseLong(adminId));
        LoginUser loginUser = new LoginUser(admin.get());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("Authentication set for user: {}", loginUser.getUsername());
        //放行
        try
        {
            chain.doFilter(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
package com.example.sews.security;

import com.example.sews.dto.Admin;
import com.example.sews.dto.LoginUser;
import com.example.sews.repo.AdminRepository;
import com.example.sews.service.AdminService;
import com.example.sews.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AdminService adminService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

//        //获取token
        String token = request.getHeader("token");
//        if (token != null ) {
//            token = token.substring(7); // 去掉"Bearer "前缀
//        }
        System.out.println("token = " + token);
        // 获取请求的URL
        String requestURL = request.getRequestURL().toString();

        // 判断URL是否包含/login或/map
        if (requestURL.contains("/login") || requestURL.contains("/map")) {
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
        //放行
        try
        {

            chain.doFilter(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
package com.example.sews.config;

import com.example.sews.security.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity //开启SpringSecurity的默认行为
@RequiredArgsConstructor//bean注解
// 新版不需要继承WebSecurityConfigurerAdapter
public class WebSecurityConfig {
 
	// 这个类主要是获取库中的用户信息，交给security
	private final UserDetailServiceImpl userDetailsService;
	// 这个的类是认证失败处理（我在这里主要是把错误消息以json方式返回）
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	// 鉴权失败的时候的处理类
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	//	登录成功处理
	private final LoginSuccessHandler loginSuccessHandler;
	//	登出成功处理
	private final LoginLogoutSuccessHandler loginLogoutSuccessHandler;
	//	token过滤器
	private final JwtTokenFilter jwtTokenFilter;
	
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration
	) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
 
	// 加密方式
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
 
	/**
	 * 核心配置
	 */

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults()	)
				//  禁用basic明文验证
				.httpBasic(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable)
				.formLogin(fl ->
						fl
								.loginPage("/map")
						.loginProcessingUrl("/home")
						.usernameParameter("username")
						.passwordParameter("password")
//						.successHandler(loginSuccessHandler)
						.permitAll())
//				.logout(lt -> lt.logoutSuccessHandler(loginLogoutSuccessHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(
						exceptions -> exceptions.authenticationEntryPoint(jwtAuthenticationEntryPoint)
								.accessDeniedHandler(jwtAccessDeniedHandler)
				)
				// 下面开始设置权限
				.authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
						// 放行 /admin/login 和 /map 路径
						.requestMatchers("/admin/login", "/map","/prediction-results/getAll").permitAll()
						// 允许所有 OPTIONS 请求
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						// 普通管理员可以访问 /devices/** 和 /model/**
						.requestMatchers("/devices/**","/model/**","/monitor/**","/warning/**","/warningRules/**","/devicesMaintenance/**","/monitoringPhoto/**","/admin/username/**","/admin/id").hasRole("ADMIN")
						// 超级管理员可以访问所有接口
						.requestMatchers("/**").hasRole("SUPER_ADMIN")
						// 其他路径需要认证
						.anyRequest().authenticated()
				)
				//  添加过滤器
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		http.headers( headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
		return http.build();
	}
	@Bean
	public UserDetailsService userDetailsService() {
		return userDetailsService::loadUserByUsername;
	}
 

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	/**
	 * 配置跨源访问(CORS)
	 *
	 * @return
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}
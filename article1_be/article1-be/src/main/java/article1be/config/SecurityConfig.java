package article1be.config;

import article1be.security.filter.JwtAuthenticationProcessingFilter;
import article1be.security.handler.JwtAccessDeniedHandler;
import article1be.security.handler.JwtAuthenticationEntryPoint;
import article1be.security.handler.OAuth2LoginFailureHandler;
import article1be.security.handler.OAuth2LoginSuccessHandler;
import article1be.security.util.JwtUtil;
import article1be.user.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록
@RequiredArgsConstructor
public class SecurityConfig {

    private final SocialLoginService socialLoginService;
    private final Environment env;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/login/oauth2/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/auth/token")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/index.html")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/webjars/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/login/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/weather/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/guest/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAuthority("ADMIN")
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/loginForm") // 프론트엔드 로그인 페이지로 설정 예정
                        .userInfoEndpoint(user -> user.userService(socialLoginService))
                        .successHandler(new OAuth2LoginSuccessHandler(env)) // JWT 발행 로직이 포함된 핸들러
                        .failureHandler(new OAuth2LoginFailureHandler())
                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // OAuth2LoginAuthenticationFilter 앞에 JwtAuthenticationProcessingFilter 추가
        http.addFilterBefore(new JwtAuthenticationProcessingFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

        // 인증, 인가 실패 핸들러 설정
        http.exceptionHandling(
                exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler(new JwtAccessDeniedHandler());
                    exceptionHandling.authenticationEntryPoint(new JwtAuthenticationEntryPoint());
                }
        );

        http.cors(cors -> cors
                .configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173"); // 허용할 도메인
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
        config.addExposedHeader("token"); // 서버 측에서 보내는 헤더에 대한 허용 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

}

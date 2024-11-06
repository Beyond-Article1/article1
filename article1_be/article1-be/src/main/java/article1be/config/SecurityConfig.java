package article1be.config;

import article1be.security.filter.JwtAuthenticationProcessingFilter;
import article1be.security.handler.OAuth2LoginSuccessHandler;
import article1be.security.util.JwtUtil;
import article1be.user.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig { // 스프링 필터 역할

    private final SocialLoginService socialLoginService;
    private final JwtUtil jwtUtil;
    private final Environment env;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소
                                .requestMatchers("/user/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        // .loginPage("/loginForm") // 프론트엔드 로그인 페이지로 설정 예정
                        .userInfoEndpoint(user -> user.userService(socialLoginService))
                        .successHandler(new OAuth2LoginSuccessHandler(env)) // JWT 발행 로직이 포함된 핸들러
                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // OAuth2LoginAuthenticationFilter 뒤에 JwtAuthenticationProcessingFilter 추가
        http.addFilterBefore(new JwtAuthenticationProcessingFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);


        /* CORS 설정 */
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
        config.addExposedHeader("token"); // 서버측에서 보내는 헤더에 대한 허용 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

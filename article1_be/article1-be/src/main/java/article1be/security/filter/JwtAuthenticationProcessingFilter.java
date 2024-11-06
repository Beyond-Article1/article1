package article1be.security.filter;

import article1be.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter{

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthenticationProcessingFilter 작동");

        // Authorization 헤더에서 JWT 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring(7); // Bearer 제거

                log.info("token 정보 확인: {}", token);

                if(jwtUtil.isTokenValid(token)) {
                    // 유효한 문자열을 기반으로 spring security에서 사용하는 authentication(인증 객체) 생성
                    Authentication authentication = jwtUtil.getAuthentication(token);
                    // JWT 토큰을 확인하는 필터가 인증 필터보다 앞에 존재. 인증 객체를 설정해 두게 되면 인증 필터 생략 (인증이 완료되었고, 이후 인증 필터는 건너 뛰게 된다.)
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("SecurityContextHolder 저장 완료");
                    log.info("Authentication : {}", authentication);
                }
            } catch (Exception e) {
                // 유효하지 않은 토큰 처리
                log.error("JWT 토큰 검증 실패: " + e.getMessage());
            }
        }

        // 다음 필터로 넘어가기
        filterChain.doFilter(request, response);
    }

}

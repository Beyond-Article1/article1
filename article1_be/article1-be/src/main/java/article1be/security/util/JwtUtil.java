package article1be.security.util;

import article1be.common.exception.CustomException;
import article1be.common.exception.ErrorCode;
import article1be.user.service.UserService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserService userService;
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    // 토큰 유효성 검사
    public boolean isTokenValid(String token) {
        try {
            // 토큰이 만료되지 않았는지 확인
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 토큰 만료 시간 검사
            return !claims.getExpiration().before(new java.util.Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("유효하지 않은 토큰입니다.");

            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    // JWT 토큰에서 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        // 토큰 Claims에서 Subject(sub)를 추출하여 userSeq 생성
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String userSeq = claims.getSubject(); // userSeq는 JWT 토큰의 subject에 존재

        // userSeq에 해당하는 사용자 정보를 데이터베이스에서 추출. 이때, UserDetails 객체가 반환
        UserDetails userDetails = userService.loadUserByUsername(userSeq);

        // UsernamePasswordAuthenticationToken 객체를 생성하여 Authentication 객체 (Spring Security가 인증 상태를 관리하는 데 사용) 생성
        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(userDetails.getAuthorities().toString()))
        );
    }

}

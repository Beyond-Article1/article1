package article1be.security.handler;

import article1be.config.PrincipalDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final Environment env;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("로그인 성공 후, security가 관리하는 principal 객체 : {}", authentication);

        // PrincipalDetails 객체로 캐스팅
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 권한을 꺼내 List<String> 으로 변환
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // 토큰에 들어갈 Claim 생성
        Claims claims = Jwts.claims().setSubject(principalDetails.getUser().getUserSeq().toString());
        claims.put("auth", authorities);

        // 서명 키를 가져오는 방법
        String secretKey = env.getProperty("token.secret");
        SecretKey signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(
                        new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time")))
                )
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        log.info("토큰 생성 확인 : {}", token);

        response.setHeader("token", token);
    }

}

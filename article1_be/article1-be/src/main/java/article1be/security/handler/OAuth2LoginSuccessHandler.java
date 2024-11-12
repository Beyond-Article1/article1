package article1be.security.handler;

import article1be.config.PrincipalDetails;
import article1be.user.entity.UserState;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final Environment env;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("로그인 성공 후, security가 관리하는 authentication 객체 : {}", authentication);

        // PrincipalDetails 객체로 캐스팅
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        log.info("회원 상태 확인: {}", principalDetails.getUser().getUserState());
        UserState userState = principalDetails.getUser().getUserState();

        if (userState.equals(UserState.DELETE)) {
            response.sendRedirect("http://localhost:80/user/delete");
        } else if (userState.equals(UserState.BAN)) {
            response.sendRedirect("http://localhost:80/user/ban");
        } else {
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

            // 쿠키에 토큰 설정
            Cookie tokenCookie = new Cookie("token", token);
            tokenCookie.setHttpOnly(false);  // 클라이언트에서 접근하지 못하도록 설정
            tokenCookie.setSecure(false);    // HTTPS 연결에서만 쿠키가 전달되도록 설정
            tokenCookie.setPath("/");       // 모든 경로에서 쿠키를 사용할 수 있도록 설정
            tokenCookie.setMaxAge(60 * 60); // 쿠키의 유효 시간 설정 (1시간)

            String newMember = "Y";

            if (principalDetails.getUser().getUserNickname() != null && principalDetails.getUser().getStyle() != null && principalDetails.getUser().getCondition() != null)
                newMember = "N";

            Cookie newMemberCookie = new Cookie("newMember", newMember);
            newMemberCookie.setHttpOnly(false);  // 클라이언트에서 접근하지 못하도록 설정
            newMemberCookie.setSecure(false);    // HTTPS 연결에서만 쿠키가 전달되도록 설정
            newMemberCookie.setPath("/");       // 모든 경로에서 쿠키를 사용할 수 있도록 설정
            newMemberCookie.setMaxAge(60 * 60); // 쿠키의 유효 시간 설정 (1시간)

            response.addCookie(tokenCookie);
            response.addCookie(newMemberCookie);

            log.info("Token Cookie - Name: {}, Value: {}, Path: {}, MaxAge: {}, Secure: {}, HttpOnly: {}",
                    tokenCookie.getName(), tokenCookie.getValue(), tokenCookie.getPath(),
                    tokenCookie.getMaxAge(), tokenCookie.getSecure(), tokenCookie.isHttpOnly());

            log.info("New Member Cookie - Name: {}, Value: {}, Path: {}, MaxAge: {}, Secure: {}, HttpOnly: {}",
                    newMemberCookie.getName(), newMemberCookie.getValue(), newMemberCookie.getPath(),
                    newMemberCookie.getMaxAge(), newMemberCookie.getSecure(), newMemberCookie.isHttpOnly());


            // 리다이렉트
            if(newMember.equals("Y")) {
                response.sendRedirect("http://localhost:80/user/data");
            }
            else {
                response.sendRedirect("http://localhost:80");
            }
        }
    }

}

package article1be.config;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행
// 로그인 진행이 완료되면 시큐리티 session 생성 (SecurityContextHolder)
// 오브젝트 => Authentication 타입 객체
// Authentication 안에 User 정보 존재
// User 오브젝트 타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails(PrincipalDetails)

import article1be.user.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// PrincipalDetails를 생성한 이유
// 시큐리티 세션 정보에는 Authentication 객체 하나만 존재 가능
// Authentication에는 두 개의 필드
// OAuth2User 타입, UserDetails 타입

// 만약 회원 가입을 한다면, 회원 가입 -> user 오브젝트가 필요
// 하지만 OAuth2User, UserDetails 타입은 user 오브젝트를 포함 X
// 세션에 저장되는데, 세션을 가지고 와도 user 오브젝트를 찾을 수 X
// 그래서 PrincipalDetails라는 클래스를 생성
// UserDetails를 Implements 해서 같은 타입으로 묶어 여기에다 user 오브젝트를 품어서 사용
// 그러면 userDetails를 대신해서 PrincipalDetails를 사용하므로 user 오브젝트에 접근 가능
// 두 개의 타입을 별도의 클래스를 사용해서 만들면 복잡해지기 때문에 부모 클래스에 같이 묶어서 꺼내 사용
// 그렇다면 꺼내 쓸 때, PrincipalDetails 타입만 꺼내서 사용
// 그 안에는 user 오브젝트가 존재

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 콤포지션
    private Map<String, Object> attributes;

    // OAuth 로그인 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user=user;
        this.attributes=attributes;
    }

    // 해당 User 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();

        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(user.getUserAuth()); // Object 타입을 String 으로 변환해서 return
            }
        });

        return collect;
    }

    @Override // name
    public String getUsername() {
        return user.getUserName();
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 만료 기간이 지났는지 여부
    @Override // 해당 계정의 비밀 번호가 1년이 지났는지 여부
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        // 1년 동안 회원이 로그인을 안 하면, 휴먼 계정
        // 현재 시간 - 로그인 시간 => 1년을 초과하면 return false;
        return true;
    }

    // OAuth2User 등록하면 getAttributes, getName 메서드가 오버라이드
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override // 사용 X
    public String getPassword() {
        return null;
    }

    @Override // 사용 X
    public String getName() {
        return null;
    }

}

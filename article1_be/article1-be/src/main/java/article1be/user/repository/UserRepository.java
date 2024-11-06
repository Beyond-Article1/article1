package article1be.user.repository;

import article1be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 회원 개인 정보 조회
    Optional<User> findByUserSeq(Long userSeq);

    User findByUserId(String userId);

    // 닉네임 중복 검증을 위한 조회
    Optional<User> findByUserNickname(String userNickname);

    // userSeq을 통해 styleName, conditionName 조회
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.style LEFT JOIN FETCH u.condition WHERE u.userSeq = :userSeq")
    Optional<User> findByUserSeqWithStyleAndCondition(Long userSeq);

    User save(User user);

}

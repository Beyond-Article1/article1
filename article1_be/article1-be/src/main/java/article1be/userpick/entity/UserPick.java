package article1be.userpick.entity;


import article1be.user.entity.UserAuth;
import article1be.user.entity.UserGender;
import article1be.user.entity.UserSocialSite;
import article1be.user.entity.UserState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "USER")
@Data
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE user SET user_state = 'DELETE', del_date = LOCALTIME WHERE user_seq = ?")
public class UserPick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;                       // 회원번호

    @Column(name = "style_seq")
    private Style styleSeq;                      // 스타일 번호

    @Column(name = "condition_seq")
    private Condition conditionSeq;                  // 체질 번호




}

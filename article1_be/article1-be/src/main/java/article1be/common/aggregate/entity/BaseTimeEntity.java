package article1be.common.aggregate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// JPA Entity들이 BaseTimeEntity를 상속할 경우 필드들도 칼럼으로 인식한다.
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // Auditing 기능을 포함시킨다.
@Getter
public abstract class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime upDate;
}


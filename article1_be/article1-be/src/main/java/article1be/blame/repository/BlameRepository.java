package article1be.blame.repository;

import article1be.blame.entity.Blame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlameRepository extends JpaRepository<Blame, Long> {
}

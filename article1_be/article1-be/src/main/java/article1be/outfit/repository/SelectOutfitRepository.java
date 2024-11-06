package article1be.outfit.repository;

import article1be.outfit.entity.Outfit;
import article1be.outfit.entity.SelectOutfit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectOutfitRepository extends JpaRepository<SelectOutfit, Long> {

    // 특정 Outfit과 UserSeq를 기준으로 선택 횟수 조회
    int countByOutfitAndSelectRecord_UserSeq(Outfit outfit, Long userSeq);
}

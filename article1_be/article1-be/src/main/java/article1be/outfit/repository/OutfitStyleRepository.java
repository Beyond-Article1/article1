package article1be.outfit.repository;

import article1be.outfit.entity.OutfitStyle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutfitStyleRepository extends JpaRepository<OutfitStyle, Long> {
    boolean existsByOutfit_OutfitSeqAndStyle_StyleSeq(Long outfitSeq, Long styleSeq);

}

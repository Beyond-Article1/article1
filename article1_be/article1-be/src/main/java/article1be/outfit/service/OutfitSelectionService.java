package article1be.outfit.service;

import article1be.outfit.dto.OutfitSelectionRequestDTO;
import article1be.outfit.entity.Outfit;
import article1be.outfit.entity.SelectRecord;
import article1be.outfit.entity.SelectOutfit;
import article1be.outfit.repository.OutfitRepository;
import article1be.outfit.repository.SelectOutfitRepository;
import article1be.outfit.repository.SelectRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OutfitSelectionService {
    private final SelectRecordRepository selectRecordRepository;
    private final SelectOutfitRepository selectOutfitRepository;
    private final OutfitRepository outfitRepository;

    @Transactional
    public void saveSelectedOutfits(OutfitSelectionRequestDTO requestDTO, Long userSeq) {
        SelectRecord savedRecord = selectRecordRepository.save(SelectRecord.create(requestDTO, userSeq));

        // Outfit 객체를 OutfitRepository에서 조회한 후 SelectOutfit 엔티티 생성 및 저장
        saveSelectedOutfit(savedRecord, requestDTO.getTopSeq());
        saveSelectedOutfit(savedRecord, requestDTO.getBottomSeq());
        saveSelectedOutfit(savedRecord, requestDTO.getShoesSeq());

        if (requestDTO.getOuterSeq() != null) {
            saveSelectedOutfit(savedRecord, requestDTO.getOuterSeq());
        }

        if (requestDTO.getAccessorySeq() != null && !requestDTO.getAccessorySeq().isEmpty()) {
            for (Long accessorySeq : requestDTO.getAccessorySeq()) {
                saveSelectedOutfit(savedRecord, accessorySeq);
            }
        }
    }

    // SelectOutfit 저장
    private void saveSelectedOutfit(SelectRecord selectRecord, Long outfitSeq) {
        Outfit outfit = outfitRepository.findById(outfitSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Outfit입니다: " + outfitSeq));

        SelectOutfit selectOutfit = SelectOutfit.create(selectRecord, outfit);
        selectOutfitRepository.save(selectOutfit);
    }
}

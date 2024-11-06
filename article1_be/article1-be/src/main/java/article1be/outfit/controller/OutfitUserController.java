package article1be.outfit.controller;

import article1be.outfit.dto.OutfitResponseDTO;
import article1be.outfit.dto.OutfitRequestDTO;
import article1be.outfit.dto.OutfitSelectionRequestDTO;
import article1be.outfit.entity.OutfitCategory;
import article1be.outfit.service.OutfitSelectionService;
import article1be.outfit.service.OutfitService;
import article1be.security.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("user/outfit")
@RequiredArgsConstructor
@Tag(name = "회원 복장 추천", description = "회원의 복장 추천 및 선택 관련 API")
public class OutfitUserController {

    private final OutfitService outfitService;
    private final OutfitSelectionService outfitSelectionService;

    @Operation(summary = "회원에 대한 날씨에 따른 복장 추천", description = "로그인한 회원에게 날씨,스타일,상황,선택 기록에 따라 적절한 복장 추천")
    @PostMapping("/recommendations")
    public ResponseEntity<Map<OutfitCategory, List<OutfitResponseDTO>>> getTop3OutfitByCategory(@RequestBody OutfitRequestDTO requestDTO) throws IOException {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        Map<OutfitCategory, List<OutfitResponseDTO>> result = outfitService.getUserRecommendedOutfits(requestDTO, userSeq);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/select")
    public ResponseEntity<String> selectOutfit(@RequestBody OutfitSelectionRequestDTO requestDTO) {
        Long userSeq = SecurityUtil.getCurrentUserSeq();
        outfitSelectionService.saveSelectedOutfits(requestDTO, userSeq);
        return ResponseEntity.ok("복장이 성공적으로 저장되었습니다.");
    }

}

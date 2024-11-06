package article1be.outfit.controller;

import article1be.outfit.dto.OutfitRequestDTO;
import article1be.outfit.dto.OutfitResponseDTO;
import article1be.outfit.dto.OutfitSelectionRequestDTO;
import article1be.outfit.entity.OutfitCategory;
import article1be.outfit.service.OutfitSelectionService;
import article1be.outfit.service.OutfitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("guest/outfit")
@RequiredArgsConstructor
@Tag(name = "비회원 복장 추천", description = "비회원의 복장 추천 및 선택 관련 API")
public class OutfitGuestController {

    private final OutfitService outfitService;

    @Operation(summary = "게스트 날씨에 따른 복장 추천", description = "게스트에게 날씨,상황에 따라 적절한 복장 추천")
    @PostMapping("/recommendations")
    public ResponseEntity<Map<OutfitCategory, List<OutfitResponseDTO>>> getTop3OutfitByCategory(@RequestBody OutfitRequestDTO requestDTO) throws IOException {
        Map<OutfitCategory, List<OutfitResponseDTO>> result = outfitService.getGuestRecommendedOutfits(requestDTO);
        return ResponseEntity.ok(result);
    }

}

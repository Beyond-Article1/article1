package article1be.outfit.service;

import article1be.openweather.dto.response.ResponseMainWeatherDTO;
import article1be.openweather.dto.response.ResponseTodayDTO;
import article1be.openweather.service.OpenWeatherService;
import article1be.outfit.dto.OutfitRequestDTO;
import article1be.outfit.dto.OutfitResponseDTO;
import article1be.outfit.entity.Outfit;
import article1be.outfit.entity.OutfitCategory;
import article1be.outfit.entity.OutfitGender;
import article1be.outfit.entity.OutfitLevel;
import article1be.outfit.repository.OutfitRepository;
import article1be.outfit.repository.OutfitSituationRepository;
import article1be.outfit.repository.OutfitStyleRepository;
import article1be.outfit.repository.SelectOutfitRepository;
import article1be.security.util.SecurityUtil;
import article1be.user.entity.Condition;
import article1be.user.entity.User;
import article1be.user.entity.UserGender;
import article1be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutfitService {
    private final OpenWeatherService weatherService;
    private final OutfitRepository outfitRepository;
    private final OutfitSituationRepository outfitSituationRepository;
    private final OutfitStyleRepository outfitStyleRepository;
    private final UserRepository userRepository;
    private final SelectOutfitRepository selectOutfitRepository;

    public Map<OutfitCategory, List<OutfitResponseDTO>> getUserRecommendedOutfits(OutfitRequestDTO requestDTO, Long userSeq) throws UnsupportedEncodingException {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

//        String requestedAtFormatted = requestDTO.getRequestedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        LocalDateTime requestedAt = requestDTO.getRequestedAt();

        ResponseMainWeatherDTO weatherData = weatherService.getMainWeatherData(
                requestedAt,
                String.valueOf(requestDTO.getLatitude()),
                String.valueOf(requestDTO.getLongitude()));

        if (weatherData == null) {
            throw new IllegalStateException("날씨 정보를 가져올 수 없습니다.");
        }

        double maxTemp = weatherData.getHighTemp();
        double minTemp = weatherData.getLowTemp();
        double pm2_5 = weatherData.getPm2_5();
        double pm_10 = weatherData.getPm10();
        int weatherCode = weatherData.getList().get(0).getWeather().get(0).getId();

        log.info("최고 온도: " + maxTemp);
        log.info("최저 온도: " + minTemp);
        log.info("날씨코드: " + weatherCode);

        boolean needMask = pm_10 >= 81 || pm2_5 >= 36;

        Condition condition = user.getCondition();
        if (condition != null) {
            if (condition.getConditionSeq() == 1) {
                maxTemp -= 3;
                minTemp -= 3;
            } else if (condition.getConditionSeq() == 2) {
                maxTemp += 3;
                minTemp += 3;
            }
        }

        UserGender userGender = user.getUserGender();
        Long styleSeq = user.getStyle() != null ? user.getStyle().getStyleSeq() : null;
        log.info("회원 스타일 번호 : "+styleSeq);

        Map<OutfitCategory, List<OutfitResponseDTO>> recommendedOutfits = new HashMap<>();

        for (OutfitCategory outfitCategory : OutfitCategory.values()) {


            List<Outfit> filteredOutfits = outfitRepository.findByCategoryAndWeatherConditions(outfitCategory, minTemp, maxTemp, weatherCode)
                    .stream()
                    .filter(outfit -> outfit.getOutfitGender() == OutfitGender.N ||
                            (userGender == UserGender.MALE && outfit.getOutfitGender() == OutfitGender.M) ||
                            (userGender == UserGender.FEMALE && outfit.getOutfitGender() == OutfitGender.F))
                    .filter(outfit -> !(!needMask && outfit.getOutfitCategory() == OutfitCategory.ACCESSORY && "마스크".equals(outfit.getOutfitName())))
                    .toList();

            List<OutfitResponseDTO> topOutfits = filteredOutfits.stream()
                    .sorted((o1, o2) -> Integer.compare(
                            calculateScore(o2, requestDTO.getSituationSeq(), styleSeq, userSeq),
                            calculateScore(o1, requestDTO.getSituationSeq(), styleSeq, userSeq)
                    ))
                    .map(outfit -> new OutfitResponseDTO(outfit.getOutfitSeq(), outfit.getOutfitName(), outfit.getOutfitImg()))
                    .collect(Collectors.toList());

            recommendedOutfits.put(outfitCategory, topOutfits);
        }

        return recommendedOutfits;
    }

    public Map<OutfitCategory, List<OutfitResponseDTO>> getGuestRecommendedOutfits(OutfitRequestDTO requestDTO) throws UnsupportedEncodingException {

//        String requestedAtFormatted = requestDTO.getRequestedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        LocalDateTime requestedAt = requestDTO.getRequestedAt();

        ResponseMainWeatherDTO weatherData = weatherService.getMainWeatherData(
                requestedAt,
                String.valueOf(requestDTO.getLatitude()),
                String.valueOf(requestDTO.getLongitude()));

        if (weatherData == null) {
            throw new IllegalStateException("날씨 정보를 가져올 수 없습니다.");
        }

        double maxTemp = weatherData.getHighTemp();
        double minTemp = weatherData.getLowTemp();
        double pm2_5 = weatherData.getPm2_5();
        double pm_10 = weatherData.getPm10();
        int weatherCode = weatherData.getList().get(0).getWeather().get(0).getId();

        log.info("최고 온도: " + maxTemp);
        log.info("최저 온도: " + minTemp);
        log.info("날씨코드: " + weatherCode);

        boolean needMask = pm_10 >= 81 || pm2_5 >= 36;

        Map<OutfitCategory, List<OutfitResponseDTO>> recommendedOutfits = new HashMap<>();

        for (OutfitCategory outfitCategory : OutfitCategory.values()) {
            log.info("카테고리 :" + outfitCategory);

            List<Outfit> filteredOutfits = outfitRepository.findByCategoryAndWeatherConditions(outfitCategory, minTemp, maxTemp, weatherCode)
                    .stream()
                    .filter(outfit -> !(!needMask && outfit.getOutfitCategory() == OutfitCategory.ACCESSORY && "마스크".equals(outfit.getOutfitName())))
                    .toList();

            List<OutfitResponseDTO> topOutfits = filteredOutfits.stream()
                    .sorted((o1, o2) -> Integer.compare(
                            calculateScore(o2, requestDTO.getSituationSeq(), null, null),
                            calculateScore(o1, requestDTO.getSituationSeq(), null, null)
                    ))
                    .map(outfit -> new OutfitResponseDTO(outfit.getOutfitSeq(), outfit.getOutfitName(), outfit.getOutfitImg()))
                    .collect(Collectors.toList());

            recommendedOutfits.put(outfitCategory, topOutfits);
        }

        return recommendedOutfits;
    }

    // 각 옷에 점수를 매기는 메서드
    private int calculateScore(Outfit outfit, Long situationSeq, Long styleSeq, Long userSeq) {
        int score = 0;

        if (outfitSituationRepository.existsByOutfit_OutfitSeqAndSituation_SituationSeq(outfit.getOutfitSeq(), situationSeq)) {
            score += 10;
        }

        if (styleSeq != null && outfitStyleRepository.existsByOutfit_OutfitSeqAndStyle_StyleSeq(outfit.getOutfitSeq(), styleSeq)) {
            score += 5;
        }

        if (outfit.getOutfitLevel() == OutfitLevel.REQUIRED) {
            score += 20;
        } else if (outfit.getOutfitLevel() == OutfitLevel.RECOMMENDED) {
            score += 10;
        }

        if (outfit.getOutfitCategory() == OutfitCategory.ACCESSORY &&
                ("마스크".equals(outfit.getOutfitName()) || "우산".equals(outfit.getOutfitName()))) {
            score += 999;
        }

        if (userSeq != null) {
            int selectionCount = selectOutfitRepository.countByOutfitAndSelectRecord_UserSeq(outfit, userSeq);
            score += selectionCount * 3; // 선택 횟수당 3점 가산점 추가
        }

        return score;
    }



}

package article1be.outfit.repository;

import article1be.outfit.entity.Outfit;
import article1be.outfit.entity.OutfitCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Long> {

    // 기존의 카테고리와 날씨 조건으로 옷을 조회하는 메서드
    @Query("SELECT o FROM Outfit o WHERE o.outfitCategory = :category " +
            "AND o.outfitTempMin <= :minTemp AND o.outfitTempMax >= :maxTemp " +
            "AND (o.outfitWeather = :weatherCode OR o.outfitWeather IS NULL)")
    List<Outfit> findByCategoryAndWeatherConditions(
            @Param("category") OutfitCategory category,
            @Param("minTemp") double minTemp,
            @Param("maxTemp") double maxTemp,
            @Param("weatherCode") int weatherCode);

}

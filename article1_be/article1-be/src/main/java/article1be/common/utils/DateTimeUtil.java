package article1be.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

// DateTime 관련 유틸 클래스
@Slf4j
public class DateTimeUtil {

    // 분까지만 포매팅
    private static final DateTimeFormatter mFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");

    // 문자열을 LocalDateTime으로 변환
    public static LocalDateTime stringParseToLocalDateTime(String inputTime) {
        return LocalDateTime.parse(inputTime, mFormatter);
    }

    // LocalDateTime을 문자열로 변환
    public static String formatToString(LocalDateTime localDateTime) {
        return localDateTime.format(mFormatter);
    }

    // dt를 우리나라 시간으로 적용해서 LocalDateTime으로 변환
    public static LocalDateTime dtParseToLocalDateTime(long dt) {

        ZoneId zoneId = ZoneId.of("Asia/Seoul");

        return Instant.ofEpochSecond(dt)
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(zoneId)
                .toLocalDateTime();
    }

    // 받은 LocalDateTime을 한국시간으로 변경
    public static LocalDateTime localDateTimeToLocalDateTime(LocalDateTime dateTime) {

        log.info("dateTime {}", dateTime.toString());

        // 한국 시간대 (UTC+9)로 변환
        LocalDateTime localDateTime = dateTime.plusHours(9);
        log.info("before localDateTime {}", localDateTime);

        // 24시를 넘어간 경우, 00시로 변경
        if (localDateTime.getHour() >= 24) {
            localDateTime = localDateTime.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        }

        return localDateTime;
    }
}

package com.hanghae.mungnayng.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtil {
    public static String convertLocaldatetimeToTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();    /* ex) 2022-09-05T10:20:30.327959 */

        /* 두 날짜(LocalDateTime) 사이의 차이 계산 until() Method
         * createdAt(등록일).until(now(현재시간), ChronoUnit(표준 날짜 기준 단위 enum 집합).enumConstant */
        long diffTime = localDateTime.until(now, ChronoUnit.SECONDS);

        if (diffTime < 60) {    /* 등록일과 현재시간 차이(초 기준)가 60초 이하면 "방금 전" return */
            return "방금 전";
        }

        diffTime = diffTime / 60;
        if (diffTime < 60) {
            return diffTime + "분 전";
        }

        diffTime = diffTime / 60;
        if (diffTime < 24) {
            return diffTime + "시간 전";
        }

        diffTime = diffTime / 24;
        if (diffTime < 30) {
            return diffTime + "일 전";
        }

        diffTime = diffTime / 30;
        if (diffTime < 12) {
            return diffTime + "개월 전";
        }

        diffTime = diffTime / 12;
        return diffTime + "년 전";
    }
}



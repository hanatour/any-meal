// Accept-Language 헤더에서 한국어 선호 여부를 판별하는 유틸리티
package com.hanatour.anymeal;

import io.micrometer.common.util.StringUtils;
import java.util.Locale;

final class LanguagePreference {

    private LanguagePreference() {
    }

    static boolean isKoreanPreferred(String acceptLanguage) {
        if (StringUtils.isEmpty(acceptLanguage)) {
            return false;
        }
        String firstLanguage = acceptLanguage.split(",", 2)[0].trim().toLowerCase(Locale.ROOT);
        return firstLanguage.equals("ko") || firstLanguage.startsWith("ko-");
    }
}

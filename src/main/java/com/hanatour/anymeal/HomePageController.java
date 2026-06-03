// 사용자 언어 환경에 맞는 정적 홈 페이지를 선택하는 컨트롤러
package com.hanatour.anymeal;

import io.micrometer.common.util.StringUtils;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class HomePageController {

    @GetMapping("/")
    public String home(
        @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        if (isKoreanPreferred(acceptLanguage)) {
            return "redirect:/ko.html";
        }
        return "forward:/index.html";
    }

    @GetMapping("/en.html")
    public String englishAlias() {
        return "redirect:/index.html";
    }

    private static boolean isKoreanPreferred(String acceptLanguage) {
        if (StringUtils.isEmpty(acceptLanguage)) {
            return false;
        }
        String firstLanguage = acceptLanguage.split(",", 2)[0].trim().toLowerCase(Locale.ROOT);
        return firstLanguage.equals("ko") || firstLanguage.startsWith("ko-");
    }
}

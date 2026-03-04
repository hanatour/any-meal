package com.hanatour.anymeal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 프론트엔드에서 사용할 공개 설정(예: 지도 API 클라이언트 ID)을 반환한다.
 */
@RestController
@RequestMapping("api")
public class ApiConfigController {

    private final String naverClientId;

    public ApiConfigController(@Value("${anymeal.naver-client-id:}") String naverClientId) {
        this.naverClientId = naverClientId != null ? naverClientId.trim() : "";
    }

    @GetMapping("config")
    public Map<String, String> config() {
        return Map.of("naverMapClientId", naverClientId);
    }
}

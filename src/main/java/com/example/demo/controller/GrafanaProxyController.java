package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/grafana")
public class GrafanaProxyController {

    @Value("${grafana.url}")
    private String grafanaUrl;

    @Value("${grafana.token}")
    private String grafanaToken;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * ✅ 1) Grafana 대시보드 정보 조회 API (React에서 JSON 데이터 받을 때 사용)
     * - 필요 없으면 삭제해도 됨
     */
    @GetMapping("/dashboard/{uid}")
    public ResponseEntity<String> getDashboard(@PathVariable String uid) {
        String targetUrl = grafanaUrl + "/api/dashboards/uid/" + uid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + grafanaToken);
        headers.set("Accept", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        log.info("🔄 Grafana 대시보드 요청 URL: {}", targetUrl);

        ResponseEntity<String> response = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        log.info("✅ Grafana 응답 상태 코드: {}", response.getStatusCode());
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    /**
     * ✅ 2) iframe URL 직접 생성 (React에서 URL만 받아서 iframe src에 쓰고 싶을 때)
     */
    @GetMapping("/embed/{uid}")
    public ResponseEntity<String> getEmbedUrl(@PathVariable String uid) {
        String embedUrl = grafanaUrl + "/d/" + uid + "/f09f9a80-solog-metric-dashboard?orgId=1&from=now-15m&to=now";
        log.info("✅ 생성된 embed URL: {}", embedUrl);
        return ResponseEntity.ok(embedUrl);
    }

    /**
     * ✅ 3) 프록시 전용: /api/grafana/** 로 들어오는 모든 요청을 Grafana로 전달
     * - 이게 핵심! static, js, css, api 전부 자동 프록시됨
     * - 이거 없으면 iframe 내부에서 /api/search, /api/user/orgs 등 전부 404 남
     */
    @GetMapping("/**")
    public ResponseEntity<byte[]> proxyAll(HttpServletRequest request) {
        // 프론트에서 들어온 전체 경로를 Grafana에 맞게 변환
        String path = request.getRequestURI().replace("/api/grafana", "");
        String targetUrl = grafanaUrl + path +
                (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        log.info("🔁 Grafana 전체 프록시 요청: {}", targetUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + grafanaToken);
        headers.set("Accept", "*/*");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                entity,
                byte[].class
        );

        // iframe 보안을 위해 X-Frame-Options 제거
        HttpHeaders proxyHeaders = new HttpHeaders();
        response.getHeaders().forEach(proxyHeaders::put);
        proxyHeaders.remove("X-Frame-Options");

        return ResponseEntity.status(response.getStatusCode())
                .headers(proxyHeaders)
                .body(response.getBody());
    }
}

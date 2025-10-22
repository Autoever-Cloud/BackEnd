package com.example.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@CrossOrigin(origins = "*") // 또는 "http://localhost:3000"
@RequestMapping("/api/grafana")
public class grafanaProxyController {

	@Value("${grafana.url}")
	private String grafanaUrl;

	@Value("${grafana.token}")
	private String grafanaToken;

	private final RestTemplate restTemplate = new RestTemplate();

	@GetMapping("/dashboard/{uid}")
	public ResponseEntity<String> getDashboard(@PathVariable String uid) {
		String targetUrl = grafanaUrl + "/api/dashboards/uid/" + uid;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + grafanaToken);
		headers.set("Accept", "application/json");

		HttpEntity<Void> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, String.class);

		return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
	}

	@GetMapping("/embed/{uid}")
	public ResponseEntity<String> getEmbedUrl(@PathVariable String uid) {
		// NOTE: slug는 실제 대시보드 URL에서 가져온 slug 사용
		String dashboardSlug = "f09f9a80-solog-metric-dashboard";
		String embedUrl = grafanaUrl + "/d/" + uid + "/" + dashboardSlug + "?orgId=1&from=now-15m&to=now&kiosk";
		return ResponseEntity.ok(embedUrl);
	}

	@GetMapping("/**")
	public ResponseEntity<byte[]> proxyAll(HttpServletRequest request) {
		String path = request.getRequestURI().replace("/api/grafana", "");
		String targetUrl = grafanaUrl + path +
				(request.getQueryString() != null ? "?" + request.getQueryString() : "");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + grafanaToken);
		headers.set("Accept", "*/*");

		HttpEntity<Void> entity = new HttpEntity<>(headers);
		ResponseEntity<byte[]> response = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, byte[].class);

		HttpHeaders proxyHeaders = new HttpHeaders();
		response.getHeaders().forEach(proxyHeaders::put);
		proxyHeaders.remove("X-Frame-Options"); // iframe 허용

		return ResponseEntity.status(response.getStatusCode())
				.headers(proxyHeaders)
				.body(response.getBody());
	}
}

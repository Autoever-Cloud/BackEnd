package com.example.demo.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.service.sseService;
import com.fasterxml.jackson.databind.JsonNode; // ❗️ JsonNode를 사용하도록 import 변경
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/aws/sns")
public class snsController {

	private final sseService sseService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public snsController(sseService sseService) {
		this.sseService = sseService;
	}

	/**
	 * AWS SNS로부터 모든 메시지를 받는 엔드포인트입니다.
	 */
	@PostMapping("/message")
	public ResponseEntity<Void> handleSnsMessage(@RequestBody String payload) {
		System.out.println("Received SNS raw payload: " + payload);

		try {
			// ❗️ 2. 받은 문자열 페이로드를 JsonNode 객체로 직접 파싱합니다.
			JsonNode rootNode = objectMapper.readTree(payload.trim());
			System.out.println("!!!! rootNode 완료");
			String messageType = rootNode.path("Type").asText();
			System.out.println("!!!!!!!!!!! Type 꺼내기 완료");

			if ("SubscriptionConfirmation".equals(messageType)) {
				// "구독 확인" 요청 처리
				String subscribeUrl = rootNode.get("SubscribeURL").asText();
				System.out.println("Confirming SNS subscription by visiting URL: " + subscribeUrl);

				new RestTemplate().getForEntity(subscribeUrl, String.class);

			} else if ("Notification".equals(messageType)) {
				System.out.println("!!!!!!!!!!! 메세지타입 확인 완료");
				System.out.println(rootNode.get("Subject"));
				
				String subjectString = rootNode.path("Subject").asText();
				String messageString = rootNode.path("Message").asText();
				System.out.println("메세지스트링바디 !!!"+messageString);
				
				

				// SseService를 통해 모든 클라이언트에게 메시지를 전달합니다.
				sseService.sendLogToClients(subjectString,messageString);
			}
		} catch (Exception e) {
			System.err.println("Error processing SNS message: " + e.getMessage());
			// 에러 처리 로직 (예: 부적절한 요청 응답)
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok().build();
	}
}
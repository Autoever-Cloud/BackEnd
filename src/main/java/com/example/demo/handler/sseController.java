package com.example.demo.handler;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.service.sseService;

@RestController
//@CrossOrigin("*")
public class sseController {

	private final sseService sseService;

	// 생성자를 통해 SseService를 주입받습니다.
	public sseController(sseService sseService) {
		this.sseService = sseService;
	}

	@GetMapping(value = "/api/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter connect() {
		return sseService.createEmitter();
	}

}
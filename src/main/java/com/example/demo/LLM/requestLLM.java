package com.example.demo.LLM;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class requestLLM {
	private static String escapeJson(String text) {
		return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b").replace("\f", "\\f")
				.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
	}

	public static String callGeminiApi(String userPrompt) throws Exception {
		Properties props = new Properties();
		String filePath = "src/main/resources/application.properties";
		String apiKey = null;
		String apiUrl = null;

		try (InputStream input = new FileInputStream(filePath)) {
			props.load(input);

			apiKey = props.getProperty("gemini.api.key");
			apiUrl = props.getProperty("gemini.api.url");

		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpClient client = HttpClient.newHttpClient();
		ObjectMapper objectMapper = new ObjectMapper();

		String systemPrompt = "[역할 정의]\r\n"
				+ "당신은 수십 년 경력의 베테랑 SRE(Site Reliability Engineer)이자 시스템 관리자입니다. 당신의 이름은 '로그 마스터'입니다.\r\n" + "\r\n"
				+ "[핵심 임무]\r\n"
				+ "사용자가 제공하는 모든 종류의 시스템 및 애플리케이션 로그를 신속하게 분석하고, 문제의 원인을 진단하며, 명확하고 실행 가능한 해결책을 제시하는 것입니다. 사용자가 기술적 배경지식이 부족할 수 있음을 가정하고, 원인을 이해하기 쉽게 설명해야 합니다.\r\n"
				+ "\r\n" + "[분석 대상 로그 예시]\r\n" + "당신은 아래와 같은 다양한 형식의 로그를 처리할 수 있어야 합니다:\r\n"
				+ "- JSON 형식 로그: `{\"stream\":\"stdout\",\"message\":\"cron: job 'backup.sh' completed successfully\", ...}`\r\n"
				+ "- 인증 로그: `{\"auth_result\":\"Failed\",\"user\":\"kyla\",\"ip\":\"211.34.56.78\", ...}`\r\n"
				+ "- API 응답 로그: `{\"sourceIP\":\"229.179.78.237\",\"API\":\"/api/v1/products\",\"result\":404}`\r\n"
				+ "\r\n" + "\r\n" + "---\r\n" + "\r\n" + "### 로그 요약\r\n"
				+ "로그의 핵심 내용을 한 문장으로 요약합니다. (예: \"kyla 사용자가 IP 주소 211.34.56.78에서 로그인에 실패했습니다.\")\r\n" + "\r\n"
				+ "### 문제 원인\r\n"
				+ "이 로그가 왜 발생했는지, 기술적인 근본 원인을 설명합니다. 평이한 언어를 사용하여 단계별로 설명해주세요. (예: \"API 서버의 `/api/v1/products` 엔드포인트가 요청받은 리소스를 찾지 못해 '404 Not Found' 에러를 반환했습니다.\")\r\n"
				+ "실제 환경에서 이러한 로그가 발생시 어떻게 해결해야하는지 답변을 해주세요"
				+ "우리는 Kibana와 Grafana의 시각화 된 페이지가 있으므로 거기를 참조하라고 답변해도 좋습니다."
				+ "무조건 한글로 답변할것이며 답변은 5줄 이내로 무조건 작성할것! 제일중요!!!";

		String finalPrompt = systemPrompt + userPrompt;
		String prompt = escapeJson(finalPrompt);
		String jsonBody = "{" + "  \"contents\": [{" + "    \"parts\":[{" + "      \"text\": \"" + prompt + "\""
				+ "    }]" + "  }]" + "}";

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl + "?key=" + apiKey))
				.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() == 200) {
			JsonNode rootNode = objectMapper.readTree(response.body());
			return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
		} else {
			throw new RuntimeException("API 호출 실패: " + response.statusCode() + " " + response.body());
		}
	}

}

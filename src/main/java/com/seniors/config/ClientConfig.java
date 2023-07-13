package com.seniors.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

	/* TODO: 추후 RestTemplate -> WebClient로 리팩토링
	- Spring Framework 6.0.0 이후로는 RestTemplate 보다 WebClient를 사용한다고 하여
	- KakaoApiClient도 수정할 계획
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
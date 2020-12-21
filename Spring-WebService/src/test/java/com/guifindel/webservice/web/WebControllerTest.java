package com.guifindel.webservice.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WebControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void mainPage_Loading() {
		//when
		String body = this.restTemplate.getForObject("/", String.class);
		
		//then
		assertThat(body).contains("스프링부트로 시작하는 웹 서비스");
		
		/**
		 * 테스트는 실제로 URL 호출시 제대로 페이지가 호출되는지에 대한 테스트이다.
		 * HTML도 결국은 규칙이 있는 문자열이기 때문에, TestRestTemplate를 통해 "/"로 호출했을 때
		 * main.hbs에 포함된 코드들이 있는지 확인하면 된다.
		 * 전체 코드를 찾을 필요는 없으며, 작성했던 "스프링부트로 시작하는 웹 서비스" 문자열이 있는지만 비교한다.
		 */
	}

}

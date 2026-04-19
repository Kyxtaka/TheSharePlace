package com.accountplace.api;

import com.accountplace.api.security.JWTProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class ApiApplicationTests {

	@Test
	void contextLoads() {
		JWTProvider jwtProvider = new JWTProvider();
		System.out.println(jwtProvider.validateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIaWthcml6c3UiLCJpYXQiOjE3MjMzODkwNzQsImV4cCI6MTcyMzM4OTY3NH0.ICftg34V30CP8oKhMt7vZjiniTE8lBmr7GT0WgC2GeM"));
		System.out.println(jwtProvider.getUsernameFromJWT("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIaWthcml6c3UiLCJpYXQiOjE3MjMzODkwNzQsImV4cCI6MTcyMzM4OTY3NH0.ICftg34V30CP8oKhMt7vZjiniTE8lBmr7GT0WgC2GeM"));
	}

}

package br.com.klimber.inova;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = { "test" })
class InovaApplicationTests {

	@Test
	void contextLoads() {
	}

}

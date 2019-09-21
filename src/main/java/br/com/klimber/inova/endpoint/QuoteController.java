package br.com.klimber.inova.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import br.com.klimber.inova.model.Quote;

@RestController
public class QuoteController {

	private static final RestTemplate restTemplate = new RestTemplate();

	@GetMapping("/quote")
	public Quote getQuote() {
		return restTemplate.getForObject("https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
	}

	@GetMapping("/quote2")
	public ResponseEntity<String> getQuote2() {
		return restTemplate.getForEntity("https://gturnquist-quoters.cfapps.io/api/random", String.class);
	}

}

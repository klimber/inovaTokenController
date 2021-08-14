package br.com.klimber.inova.service;

import br.com.klimber.inova.model.AvailableFeatures;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AvailableFeatureService {

    public static final String GET_AVAILABLE_FEATURES_URL = "https://api.powerbi.com/v1.0/myorg/availableFeatures";
    private final AzureTokenService tokenService;
    private final RestTemplate restTemplate;

    private static AvailableFeatures availableFeatures;

    public AvailableFeatures getAvailableFeatures() {
        if(availableFeatures == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(tokenService.getToken());
            HttpEntity<Object> request = new HttpEntity<>(headers);
            availableFeatures = restTemplate.exchange(GET_AVAILABLE_FEATURES_URL, HttpMethod.GET, request,
                                                      AvailableFeatures.class).getBody();
        }
        return availableFeatures;
    }

    public void clearAvailableFeatures() {
        availableFeatures = null;
    }
}

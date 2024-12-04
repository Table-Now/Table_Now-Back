package zerobase.tableNow.domain.store.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import zerobase.tableNow.domain.store.dto.KakaoApiResponse;

@Service
public class LocationService { //카카오 API 활용

    private final WebClient webClient;

    public LocationService(@Value("${kakao.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader("Authorization", "KakaoAK " + apiKey)
                .build();
    }

    public double[] getCoordinates(String keyword) {
        // 키워드 검색 엔드포인트로 변경
        String uri = "/v2/local/search/keyword.json?query=" + keyword;

        KakaoApiResponse response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(KakaoApiResponse.class)
                .block();

        // 응답이 존재하고 검색 결과가 비어있지 않을 경우 좌표 반환
        if (response != null && !response.getDocuments().isEmpty()) {
            double latitude = Double.parseDouble(String.valueOf(response.getDocuments().get(0).getY()));
            double longitude = Double.parseDouble(String.valueOf(response.getDocuments().get(0).getX()));
            return new double[]{latitude, longitude};
        }

        // 좌표를 찾지 못할 경우 예외 발생
        throw new RuntimeException("Coordinates not found for address: " + keyword);
    }
}

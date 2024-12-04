package zerobase.tableNow.domain.store.dto;

import lombok.Data;
import java.util.List;

@Data
public class KakaoApiResponse {
    private List<Document> documents;

    @Data
    public static class Document {
        private String addressName;
        private double x;  // 경도
        private double y;  // 위도
    }
}

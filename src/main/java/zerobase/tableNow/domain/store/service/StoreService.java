package zerobase.tableNow.domain.store.service;

import org.springframework.web.multipart.MultipartFile;
import zerobase.tableNow.domain.constant.SortType;
import zerobase.tableNow.domain.store.dto.StoreDto;

import java.util.List;

public interface  StoreService {


    //상점 등록
    StoreDto register(StoreDto storeDto, MultipartFile image);

    //상점 목록
    List<StoreDto> getAllStores(String keyword, SortType sortTypeEnum, Double userLat, Double userLon);

    //상점 수정
    StoreDto update(Long id,StoreDto storeDto);

    //상점 상세정보
    StoreDto detail(Long id);

    //상점 삭제
    void delete(Long id);


}

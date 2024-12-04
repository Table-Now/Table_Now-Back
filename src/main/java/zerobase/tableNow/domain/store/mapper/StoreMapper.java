package zerobase.tableNow.domain.store.mapper;

import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.store.dto.StoreDto;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Component
public class StoreMapper {
    public StoreEntity toStoreEntity(StoreDto storeDto, UsersEntity optionalUsers) {
        //상점등록 DTO -> Entity
        return StoreEntity.builder()
                .user(optionalUsers)
                .store(storeDto.getStore())
                .storeLocation(storeDto.getStoreLocation())
                .storeImg(storeDto.getStoreImg())
                .storeContents(storeDto.getStoreContents())
                .storeOpen(storeDto.getStoreOpen())
                .storeClose(storeDto.getStoreClose())
                .storeWeekOff(storeDto.getStoreWeekOff())
                .latitude(storeDto.getLatitude())
                .longitude(storeDto.getLongitude())
                .build();

    }

    //상점등록 Entity -> Dto
    public StoreDto toStoreDto(StoreEntity storeEntity) {
        return StoreDto.builder()
                .user(storeEntity.getUser().getUser())
                .store(storeEntity.getStore())
                .storeLocation(storeEntity.getStoreLocation())
                .storeImg(storeEntity.getStoreImg())
                .storeContents(storeEntity.getStoreContents())
                .storeOpen(storeEntity.getStoreOpen())
                .storeClose(storeEntity.getStoreClose())
                .storeWeekOff(storeEntity.getStoreWeekOff())
                .latitude(storeEntity.getLatitude())
                .longitude(storeEntity.getLongitude())
                .build();
    }

    //상점 목록 Entity를 DTO로 변환
    public StoreDto convertToDto(StoreEntity storeEntity) {
        return StoreDto.builder()
                .id(storeEntity.getId())
                .user(storeEntity.getUser().getUser())
                .store(storeEntity.getStore())
                .storeLocation(storeEntity.getStoreLocation())
                .storeImg(storeEntity.getStoreImg())
                .storeContents(storeEntity.getStoreContents())
                .rating(storeEntity.getRating())
                .storeOpen(storeEntity.getStoreOpen())
                .storeClose(storeEntity.getStoreClose())
                .storeWeekOff(storeEntity.getStoreWeekOff())
                .createAt(storeEntity.getCreateAt())
                .updateAt(storeEntity.getUpdateAt())
                .latitude(storeEntity.getLatitude())
                .longitude(storeEntity.getLongitude())
                .distance(storeEntity.getDistance())
                .build();
    }
}

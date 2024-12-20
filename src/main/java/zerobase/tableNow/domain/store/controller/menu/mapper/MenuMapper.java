package zerobase.tableNow.domain.store.controller.menu.mapper;

import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuDto;
import zerobase.tableNow.domain.store.controller.menu.entity.MenuEntity;
import zerobase.tableNow.domain.store.entity.StoreEntity;

@Component
public class MenuMapper {
    public MenuEntity toMenuEntity(MenuDto menuDto,
                                   StoreEntity store){
        // 메뉴의 가격과 수량을 곱하여 총합을 계산
        int totalCount = menuDto.getPrice() * menuDto.getCount();

        return MenuEntity.builder()
                .storeId(store)
                .image(menuDto.getImage())
                .name(menuDto.getName())
                .price(menuDto.getPrice())
                .status(Status.ING)
                .count(totalCount)
                .build();
    }

    public MenuDto toMenuDto(MenuEntity menu){
        return MenuDto.builder()
                .storeId(menu.getStoreId().getId())
                .image(menu.getImage())
                .name(menu.getName())
                .price(menu.getPrice())
                .status(menu.getStatus())
                .count(menu.getCount())
                .build();
    }
}

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
        return MenuEntity.builder()
                .storeId(store)
                .image(menuDto.getImage())
                .name(menuDto.getName())
                .price(menuDto.getPrice())
                .status(Status.ING)
                .build();
    }

    public MenuDto toMenuDto(MenuEntity menu){
        return MenuDto.builder()
                .storeId(menu.getStoreId().getId())
                .image(menu.getImage())
                .name(menu.getName())
                .price(menu.getPrice())
                .status(menu.getStatus())
                .build();
    }
}

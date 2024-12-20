package zerobase.tableNow.domain.store.controller.menu.service;

import org.springframework.web.multipart.MultipartFile;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuDto;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuUpdateDto;

import java.util.List;

public interface MenuService {
    MenuDto register(MenuDto menuDto, MultipartFile image);

    List<MenuDto> list(Long storeId);

    void delete(Long menuId);

    void update(Long menuId, MenuUpdateDto menuUpdateDto, MultipartFile image);

    void reStatus(Long menuId);
}

package zerobase.tableNow.domain.store.controller.menu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuDeleteDto;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuDto;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuUpdateDto;
import zerobase.tableNow.domain.store.controller.menu.service.MenuService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/")
public class MenuController {
    private final MenuService menuService;

    //메뉴등록
    @PostMapping("register")
    public ResponseEntity<MenuDto> register(@RequestBody MenuDto menuDto){
        return ResponseEntity.ok().body(menuService.register(menuDto));
    }

    //메뉴목록
    @GetMapping("list")
    public ResponseEntity<List<MenuDto>> list(@RequestParam(name = "storeId") Long storeId){
        return ResponseEntity.ok(menuService.list(storeId));
    }

    // 메뉴 삭제
    @DeleteMapping("delete")
    public ResponseEntity<String> delete(@RequestParam(name = "id") Long id) {
        menuService.delete(id);
        return ResponseEntity.ok("success");
    }
    //메뉴 수정
    @PutMapping("update")
    public ResponseEntity<String > update(@RequestBody MenuUpdateDto menuUpdateDto){
        menuService.update(menuUpdateDto);
        return ResponseEntity.ok("success");
    }
    // 메뉴 수정 -> 상태수정 (매진여부)
    @PutMapping("restatus")
    public ResponseEntity<String> reStatus(@RequestParam(name = "menuId") Long menuId){
        menuService.reStatus(menuId);
        return ResponseEntity.ok("success");
    }

}

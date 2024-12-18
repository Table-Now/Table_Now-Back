package zerobase.tableNow.domain.store.controller.menu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuDto;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuUpdateDto;
import zerobase.tableNow.domain.store.controller.menu.service.MenuService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/menus/")
public class MenuController {
    private final MenuService menuService;

    //메뉴등록
    @PostMapping("register")
    public ResponseEntity<MenuDto> menuCreate(
            @RequestBody MenuDto menuDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ){
        return ResponseEntity.ok().body(menuService.register(menuDto, image));
    }

    //메뉴목록
    @GetMapping("menu/{storeId}")
    public ResponseEntity<List<MenuDto>> list(@PathVariable(name = "storeId") Long storeId){
        return ResponseEntity.ok(menuService.list(storeId));
    }

    // 메뉴 삭제
    @DeleteMapping("{menuId}")
    public ResponseEntity<String> delete(@PathVariable(name = "menuId") Long menuId) {
        menuService.delete(menuId);
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

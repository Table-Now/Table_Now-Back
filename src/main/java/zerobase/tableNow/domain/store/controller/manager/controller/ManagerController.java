package zerobase.tableNow.domain.store.controller.manager.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase.tableNow.domain.store.controller.manager.dto.ConfirmDto;
import zerobase.tableNow.domain.store.controller.manager.dto.ManagerDto;
import zerobase.tableNow.domain.store.controller.manager.service.ManagerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/manager/")
public class ManagerController {
    private final ManagerService managerService;

    // 해당 매니저의 상점 목록 조회
    @GetMapping("managers/{user}/list")
    public ResponseEntity<List<ManagerDto>> managerList(@PathVariable(name = "user") String user) {
        List<ManagerDto> managerDtoList = managerService.managerList(user);
        return ResponseEntity.ok(managerDtoList);
    }

    // 예약 정보 확인
    @GetMapping("managers/{store}/reservations")
    public ResponseEntity<List<ConfirmDto>> confirmList(@PathVariable(name = "store") String store) {
        List<ConfirmDto> confirmList = managerService.getWaitingList(store);
        return ResponseEntity.ok().body(confirmList);
    }
}

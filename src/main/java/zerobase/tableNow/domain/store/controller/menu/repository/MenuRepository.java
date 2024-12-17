package zerobase.tableNow.domain.store.controller.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.store.controller.menu.entity.MenuEntity;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity,Long> {
    List<MenuEntity> findByStoreId_Id(Long storeId);

    // 상태 변경 메서드 추가 (매진 -> 진행 중, 진행 중 -> 매진)
    @Modifying
    @Query("UPDATE MenuEntity m SET m.status = :status WHERE m.id = :menuId")
    void updateMenuStatus(@Param("menuId") Long menuId, @Param("status") Status status);
}

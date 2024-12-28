package zerobase.tableNow.domain.store.controller.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.store.controller.manager.entity.SettlementEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SettlementRepository extends JpaRepository<SettlementEntity,Long> {
    List<SettlementEntity> findAllByStoreIdAndCreateAtBetween(Long storeId, LocalDateTime localDateTimeOfFrom, LocalDateTime localDateTimeOfTo);
}

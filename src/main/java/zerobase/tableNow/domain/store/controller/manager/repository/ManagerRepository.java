package zerobase.tableNow.domain.store.controller.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.store.controller.manager.dto.ConfirmDto;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.List;

@Repository
public interface ManagerRepository extends JpaRepository<ReservationEntity,Long> {
    List<ReservationEntity> findByUser(UsersEntity userId);
}
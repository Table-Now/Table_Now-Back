package zerobase.tableNow.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    //폰번호으로 예약 승인
    Optional<ReservationEntity> findByPhone(String phone);

    boolean existsByUserAndStore(
            UsersEntity user
            , StoreEntity store);

    List<ReservationEntity> findByUser(UsersEntity user);

    boolean existsByStore(StoreEntity store);

    boolean existsByUserAndStoreNot(UsersEntity users, StoreEntity store);

    ReservationEntity findByUser_User(String userId);

    List<ReservationEntity> findByStore_Store(String store);
}
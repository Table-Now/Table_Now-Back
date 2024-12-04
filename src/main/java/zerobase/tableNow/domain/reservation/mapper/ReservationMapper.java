package zerobase.tableNow.domain.reservation.mapper;

import org.springframework.stereotype.Component;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.reservation.dto.ReservationDto;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

@Component
public class ReservationMapper {
    public ReservationEntity toReserEntity(ReservationDto reservationDto,
                                           UsersEntity optionalUsers,
                                           StoreEntity optionalStore){
        //예약 요청 DTO -> Entity
        return ReservationEntity.builder()
                .user(optionalUsers)
                .phone(optionalUsers.getPhone())
                .store(optionalStore)
                .reservationDateTime(reservationDto.getReservationDateTime())
                .peopleNb(reservationDto.getPeopleNb())
                .reservationStatus(Status.REQ)
                .build();

    }
    public ReservationDto toReserDto(ReservationEntity reservationEntity){
        return ReservationDto.builder()
                .id(reservationEntity.getId())
                .userId(reservationEntity.getUser().getUser())
                .phone(reservationEntity.getPhone())
                .store(reservationEntity.getStore().getStore())
                .reservationDateTime(reservationEntity.getReservationDateTime())
                .peopleNb(reservationEntity.getPeopleNb())
                .reservationStatus(reservationEntity.getReservationStatus())
                .build();
    }
}

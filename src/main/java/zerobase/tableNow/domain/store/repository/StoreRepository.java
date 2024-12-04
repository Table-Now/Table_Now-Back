package zerobase.tableNow.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity,Long> {

    //매니저전용 상점 목록
    List<StoreEntity> findByUser(UsersEntity user);

    Optional<StoreEntity> findByStore(String store);

    List<StoreEntity> findByStoreContainingIgnoreCase(String keyword);


}


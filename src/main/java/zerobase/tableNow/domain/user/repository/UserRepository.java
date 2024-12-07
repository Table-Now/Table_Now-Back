package zerobase.tableNow.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import zerobase.tableNow.domain.user.entity.UsersEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity,Long> {
    Optional<UsersEntity> findByUser(String user);

    long countByEmail(String email);

    Optional<UsersEntity> findByEmail(String userEmail);
}


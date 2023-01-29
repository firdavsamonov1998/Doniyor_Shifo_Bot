package com.example.owner.repository;

import com.example.entity.UsersEntity;
import com.example.enums.Status;
import com.example.enums.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<UsersEntity, Long> {

    Optional<UsersEntity> findByUserIdAndStatus(Long userId, Status status);
    Optional<UsersEntity> findByPasswordAndStatus(String password, Status status);

    boolean existsByPassword(String password);

    List<UsersEntity> findAllByRole(UserRole role);

    boolean existsByPhone(String phone);

    boolean existsByUserId(Long userId);




    boolean existsByUserIdAndStatus(Long userId, Status active);
}

package com.example.owner.repository;

import com.example.entity.PatientEntity;
import com.example.enums.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepository extends CrudRepository<PatientEntity, Long> {


//    @Query(value = "select * from patient " +
//            " where  full_name ilike :name",nativeQuery = true)
    List<PatientEntity> findByFullNameContainingIgnoreCase( String name);

    Integer countByFloorStartingWithAndStatus(String floor,Status status);

    List<PatientEntity> findAllByStatus(Status status);

    List<PatientEntity> getByFullNameContainingIgnoreCaseAndStatus(String name,Status status);


}

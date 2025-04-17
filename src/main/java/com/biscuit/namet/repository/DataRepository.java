package com.biscuit.namet.repository;

import com.biscuit.namet.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataRepository extends JpaRepository<DataEntity, Long> {

    Optional<DataEntity> findFirstByOrderByCreatedAtDesc();
}

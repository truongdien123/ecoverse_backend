package com.fpt.ecoversewasteitem.repositories;

import com.fpt.ecoversewasteitem.entities.WasteBin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WasteBinRepository extends JpaRepository<WasteBin, String> {

    Optional<WasteBin> findByCode(String code);

    boolean existsByCode(String code);
}

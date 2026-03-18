package com.fpt.ecoverseauth.repositories;

import com.fpt.ecoverseuser.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnershipRepository extends JpaRepository<Partner, String> {
    Optional<Partner> findByEmail(String email);
    boolean existsByEmail(String email);
}

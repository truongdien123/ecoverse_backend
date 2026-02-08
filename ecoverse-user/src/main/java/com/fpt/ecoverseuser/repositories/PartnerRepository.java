package com.fpt.ecoverseuser.repositories;

import com.fpt.ecoverseuser.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, String> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}

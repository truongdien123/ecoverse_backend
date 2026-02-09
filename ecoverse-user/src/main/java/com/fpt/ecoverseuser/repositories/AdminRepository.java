package com.fpt.ecoverseuser.repositories;

import com.fpt.ecoverseuser.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}

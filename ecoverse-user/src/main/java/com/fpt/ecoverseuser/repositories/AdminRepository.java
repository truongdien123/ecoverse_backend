package com.fpt.ecoverseuser.repositories;

import com.fpt.ecoverseuser.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface AdminRepository extends JpaRepository<Admin, String> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select a.email from Admin a where a.email in :mails")
    Set<String> findExistingEmails(@Param("mails") Set<String> mails);
}

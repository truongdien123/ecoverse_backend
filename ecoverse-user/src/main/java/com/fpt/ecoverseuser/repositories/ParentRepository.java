package com.fpt.ecoverseuser.repositories;

import com.fpt.ecoverseuser.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ParentRepository extends JpaRepository<Parent, String> {

    @Query("select count(p.id) from Parent p where p.partner.id = :partnerId")
    long countParentsByPartnerId(@Param("partnerId") String partnerId);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select p.email from Parent p where p.email in :emails")
    Set<String> findExistingEmails(@Param("emails") Set<String> mails);
}

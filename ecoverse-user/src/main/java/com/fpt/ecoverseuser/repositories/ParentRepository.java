package com.fpt.ecoverseuser.repositories;

import com.fpt.ecoverseuser.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent, String> {

    @Query("select count(p.id) from Parent p where p.partner.id = :partnerId")
    long countParentsByPartnerId(@Param("partnerId") String partnerId);
}

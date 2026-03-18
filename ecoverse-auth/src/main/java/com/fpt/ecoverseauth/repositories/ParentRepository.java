package com.fpt.ecoverseauth.repositories;

import com.fpt.ecoverseuser.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, String> {
    Optional<Parent> findByEmail(String email);
    boolean existsByEmail(String email);
}

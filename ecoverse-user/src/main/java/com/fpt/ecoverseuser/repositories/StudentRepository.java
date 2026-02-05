package com.fpt.ecoverseuser.repositories;

import com.fpt.ecoverseuser.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    @Query("select count(st.id) from Student st where st.partner.id = :partnerId")
    long countStudentByPartnerId(@Param("partnerId") String partnerId);
}

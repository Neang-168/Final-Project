
package com.example.finalprojectforjava.repositories;

import com.example.finalprojectforjava.entities.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,Long>{
    
}

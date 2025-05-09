
package com.example.finalprojectforjava.repositories;

import com.example.finalprojectforjava.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long>{
    boolean existsByIsbn(String isbn);
}

package com.example.sews.repo;

import com.example.sews.dto.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUsername(String username);

    @Query("select a from Admin a where a.role = 3")
    List<Admin> findUser();

}

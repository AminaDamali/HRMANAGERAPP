package com.MercureIT.HR_Manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.MercureIT.HR_Manager.models.User;

public interface NewUserRepository extends JpaRepository<User, Integer> {
    // You can add custom query methods here if needed
}

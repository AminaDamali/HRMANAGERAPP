package com.MercureIT.HR_Manager.repositories;

import com.MercureIT.HR_Manager.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    // Search clients by first name, last name, or email
    List<Client> findByFirstnameContainingOrLastnameContainingOrEmailContaining(String firstname, String lastname, String email);
}

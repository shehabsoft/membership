package org.emu.membership.elastic.jpa;

import org.emu.membership.elastic.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepositoryForDb extends JpaRepository<Customer, String> {

    List<Customer> findByName(String name);

    List<Customer> findByNameContaining(String name);

}

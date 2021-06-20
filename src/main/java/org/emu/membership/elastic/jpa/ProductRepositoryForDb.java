package org.emu.membership.elastic.jpa;



import org.emu.membership.elastic.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepositoryForDb extends JpaRepository<Product, String> {

    List<Product> findByName(String name);

    List<Product> findByNameContaining(String name);

    List<Product> findByManufacturerAndCategory(String manufacturer,String category);
}

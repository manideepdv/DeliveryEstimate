package com.example.ecom.repositories;

import com.example.ecom.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {
    @Override
    Optional<Seller> findById(Integer integer);
}

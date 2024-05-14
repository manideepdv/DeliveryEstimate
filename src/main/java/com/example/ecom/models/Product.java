package com.example.ecom.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Product extends BaseModel{
    private String name;
    private String description;
    private double price;

    @ManyToOne
    private Seller seller;
}

package com.example.shopappbackend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Product name is not empty")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Unit price can not be null")
    @Column(name = "unit_price", nullable = false)
    private float unitPrice;

    @Column(name = "is_deleted" , nullable = false)
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}

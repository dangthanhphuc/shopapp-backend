package com.example.shopappbackend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Image url can not be empty")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;


    @Column(name = "is_deleted" , nullable = false)
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}

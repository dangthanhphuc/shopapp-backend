package com.example.shopappbackend.services.product;


import com.example.shopappbackend.dtos.ProductDTO;
import com.example.shopappbackend.entities.Product;
import com.example.shopappbackend.entities.ProductImage;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(long id) throws DataNotFoundException;
    Product getProductById(long id) throws DataNotFoundException;
    Page<Product> getProductsByKeyword(String keyword, Long categoryId, PageRequest request);
    ProductImage createProductImage(Long id, MultipartFile file) throws DataNotFoundException, IOException ;

}

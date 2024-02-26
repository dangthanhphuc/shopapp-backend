package com.example.shopappbackend.services.product;

import com.example.shopappbackend.dtos.ProductDTO;
import com.example.shopappbackend.entities.Category;
import com.example.shopappbackend.entities.Material;
import com.example.shopappbackend.entities.Product;
import com.example.shopappbackend.entities.ProductImage;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.CategoryRepository;
import com.example.shopappbackend.repositories.MaterialRepository;
import com.example.shopappbackend.repositories.ProductImageRepository;
import com.example.shopappbackend.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final MaterialRepository materialRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find category wit id: " + productDTO.getCategoryId())
        );

        Material existingMaterial = materialRepository.findById(productDTO.getMaterialId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find material wit id: " + productDTO.getMaterialId())
        );

        Product newProduct = productRepository.save(
                Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .unitPrice(productDTO.getUnitPrice())
                .category(existingCategory)
                .material(existingMaterial)
                .build()
        );

        return productRepository.save(newProduct);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find category with id: " + productDTO.getCategoryId())
        );

        Material existingMaterial = materialRepository.findById(productDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find category with id: " + productDTO.getMaterialId())
        );


        Product existingProduct = findProduct(id);

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setUnitPrice(productDTO.getUnitPrice());
        existingProduct.setCategory(existingCategory);
        existingProduct.setMaterial(existingMaterial);
        existingProduct.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(long id) throws DataNotFoundException {
        Product existingProduct = findProduct(id);
        existingProduct.setDeleted(true);
        productRepository.save(existingProduct);
    }

    @Override
    public Product getProductById(long id) throws DataNotFoundException {
        return findProduct(id);
    }

    @Override
    public Page<Product> getProductsByKeyword(String keyword, Long categoryId, PageRequest request) {
        return productRepository.searchProducts(categoryId, keyword, request);
    }

    public Product findProduct(long id) throws DataNotFoundException {
        return productRepository.findById(id).stream()
                .filter(product -> !product.isDeleted())
                .findFirst()
                .orElseThrow(
                        () -> new DataNotFoundException("Cannot find product wit id: " + id)
                );
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductImage createProductImage(Long id, MultipartFile file) throws DataNotFoundException, IOException {

        Product existingProduct = findProduct(id);

        String urlImages = storeFile(file);

        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(urlImages)
                .build();

        return productImageRepository.save(newProductImage);

    }

    private String storeFile(MultipartFile file) throws IOException {


        // Lấy tên file
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + '_' + filename;
        // Đường dẫn đến thư mục bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        //Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename;
    }
}

package com.example.shopappbackend.controllers;

import com.example.shopappbackend.dtos.ProductDTO;
import com.example.shopappbackend.entities.Product;
import com.example.shopappbackend.entities.ProductImage;
import com.example.shopappbackend.responses.product.ProductListResponse;
import com.example.shopappbackend.responses.product.ProductResponse;
import com.example.shopappbackend.services.product.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById (@PathVariable("id") Long productId){
        ProductResponse productResponse = new ProductResponse();
        try{
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(product));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProductsByKeyWord (
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
//            @RequestParam(defaultValue = "id,desc") String[] sort
    ){
        // Tạo PageRequest
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").descending()
        );
        // Lấy Page
        ProductListResponse productListResponse = ProductListResponse.builder()
                .products(productService.getProductsByKeyword(keyword, categoryId, pageRequest).stream()
                        .map(ProductResponse::fromProduct)
                        .toList())
                .build();

        return ResponseEntity.ok().body(productListResponse);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> createProduct (
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ){
        try {
            if(result.hasErrors()){
                Map<String, String> errors = result.getFieldErrors().stream()
                        .filter(error -> error.getDefaultMessage() != null)
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                return ResponseEntity.badRequest().body(errors);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(ProductResponse.fromProduct(newProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ){
        List<ProductImage> productImages = new ArrayList<ProductImage>();
        try {
            for (MultipartFile file : files){
                // Kiểm tra kích thước < 10MB
                if(file.getSize() > 10 * 1024 * 1024){
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }
                // Kiểm tra dịnh dạng file có phải file ảnh không
                if(file.getContentType() == null || !file.getContentType().startsWith("image/")){
                    // error
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                ProductImage productImage = productService.createProductImage(productId, file);
                productImages.add(productImage);
                return ResponseEntity.ok().body(productImages);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long productId,
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ){
        try {
            if(result.hasErrors()){
                Map<String, String> errors = result.getFieldErrors().stream()
                        .filter(error -> error.getDefaultMessage() != null)
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                return ResponseEntity.badRequest().body(errors);
            }
            Product updatedProduct = productService.updateProduct(productId, productDTO);
            return ResponseEntity.ok().body(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteProductById(
            @PathVariable("id") Long productId
    ) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok().body("Deleted product successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<?> viewImage(
            @PathVariable String imageName
    ){
        try{
            Path imagePath = Paths.get("uploads/" + imageName); // Path của folder upload
            UrlResource urlResource = new UrlResource(imagePath.toUri()); // Lấy Url nguồn tới file hiêện tại (VD: C://... + imagePath)

            if(urlResource.exists()){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(urlResource);
            } else{
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Chức năng chưa xử lý (chưa hiểu)
    //generateFakeProducts

}

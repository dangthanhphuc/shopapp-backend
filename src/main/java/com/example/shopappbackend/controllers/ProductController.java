package com.example.shopappbackend.controllers;

import com.example.shopappbackend.dtos.ProductDTO;
import com.example.shopappbackend.entities.Product;
import com.example.shopappbackend.entities.ProductImage;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.responses.ResponseObject;
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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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
    public ResponseEntity<ResponseObject> getProductById (@PathVariable("id") Long productId) throws DataNotFoundException {

        Product product = productService.getProductById(productId);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully get product by id !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(ProductResponse.fromProduct(product))
                        .build()
        );
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createProduct (
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) throws DataNotFoundException {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .filter(error -> error.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Successfully get product by id !")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .data(errors)
                            .build()
            );
        }
        Product newProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully create product !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(ProductResponse.fromProduct(newProduct))
                        .build()
        );

    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ) throws DataNotFoundException, IOException {
        List<ProductImage> productImages = new ArrayList<ProductImage>();

        for (MultipartFile file : files) {
            // Kiểm tra kích thước < 10MB
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(
                        ResponseObject.builder()
                                .timeStamp(LocalDateTime.now())
                                .message("File is too large! Maximum size is 10MB")
                                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                                .statusCode(HttpStatus.PAYLOAD_TOO_LARGE.value())
                                .build()
                );
            }
            // Kiểm tra dịnh dạng file có phải file ảnh không
            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                // error
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
                        ResponseObject.builder()
                                .timeStamp(LocalDateTime.now())
                                .message("File must be an image")
                                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                                .build()
                );
            }
            ProductImage productImage = productService.createProductImage(productId, file);
            productImages.add(productImage);

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully upload images !")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .data(productImages)
                        .build()
        );

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> updateProduct(
            @PathVariable("id") Long productId,
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) throws DataNotFoundException {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .filter(error -> error.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Input is invalid !")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .data(errors)
                            .build()
            );
        }
        Product updatedProduct = productService.updateProduct(productId, productDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully update product !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(updatedProduct)
                        .build()
        );

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> deleteProductById(
            @PathVariable("id") Long productId
    ) throws DataNotFoundException {

        productService.deleteProduct(productId);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully deleted product !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
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

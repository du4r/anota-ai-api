package com.anotaai.anotaai.services;

import com.anotaai.anotaai.domain.category.Category;
import com.anotaai.anotaai.domain.category.exceptions.CategoryNotFoundException;
import com.anotaai.anotaai.domain.category.exceptions.ProductNotFoundException;
import com.anotaai.anotaai.domain.product.Product;
import com.anotaai.anotaai.domain.product.ProductDTO;
import com.anotaai.anotaai.repositories.ProductRepository;
import com.anotaai.anotaai.services.aws.AwsSnsService;
import com.anotaai.anotaai.services.aws.MessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private CategoryService categoryService;

    private AwsSnsService awsSnsService;

    public ProductService(ProductRepository repository, CategoryService categoryService, AwsSnsService awsSnsService) {
        this.productRepository = repository;
        this.categoryService = categoryService;
        this.awsSnsService = awsSnsService;
    }

    public Product insert(ProductDTO productData) {
        Category category = this.categoryService.getById(productData.categoryId()).orElseThrow(CategoryNotFoundException::new);
        Product newProduct = new Product(productData);
        newProduct.setCategory(category);
        this.productRepository.save(newProduct);
        this.awsSnsService.publish(new MessageDTO(newProduct.getOwnerId()));
        return newProduct;
    }

    public List<Product> getAll() {
        return this.productRepository.findAll();
    }

    public Product update(String id, ProductDTO productData) {
        Product product = this.productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        this.categoryService.getById(productData.categoryId()).ifPresent(product::setCategory);

        if (!productData.title().isEmpty()) product.setTitle(productData.title());
        if (!productData.description().isEmpty()) product.setDescription(productData.description());
        if (!(productData.price() == null)) product.setPrice(productData.price());

        this.productRepository.save(product);

        this.awsSnsService.publish(new MessageDTO(product.getOwnerId()));
        return product;
    }

    public void delete(String id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
        this.productRepository.delete(product);
    }
}

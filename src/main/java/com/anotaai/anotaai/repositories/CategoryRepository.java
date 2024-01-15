package com.anotaai.anotaai.repositories;

import com.anotaai.anotaai.domain.category.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category,String> {
}

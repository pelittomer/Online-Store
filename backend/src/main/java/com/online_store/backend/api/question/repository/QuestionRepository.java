package com.online_store.backend.api.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.question.entities.Question;
import java.util.List;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.company.entities.Company;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByProduct(Product product);

    List<Question> findByCompany(Company company);
}

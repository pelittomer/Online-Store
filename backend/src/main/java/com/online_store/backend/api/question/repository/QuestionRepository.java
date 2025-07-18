package com.online_store.backend.api.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.question.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}

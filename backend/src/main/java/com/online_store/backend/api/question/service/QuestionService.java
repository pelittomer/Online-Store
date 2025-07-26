package com.online_store.backend.api.question.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.company.entities.Company;
import com.online_store.backend.api.company.utils.CompanyUtilsService;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.repository.ProductRepository;
import com.online_store.backend.api.question.dto.request.AnswerRequestDto;
import com.online_store.backend.api.question.dto.request.QuestionRequestDto;
import com.online_store.backend.api.question.dto.response.QuestionResponseDto;
import com.online_store.backend.api.question.entities.Question;
import com.online_store.backend.api.question.repository.QuestionRepository;
import com.online_store.backend.api.question.utils.mapper.CreateAnswerMapper;
import com.online_store.backend.api.question.utils.mapper.CreateQuestionMapper;
import com.online_store.backend.api.question.utils.mapper.GetQuestionMapper;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final CommonUtilsService commonUtilsService;
    private final ProductRepository productRepository;
    private final CreateQuestionMapper createQuestionMapper;
    private final CreateAnswerMapper createAnswerMapper;
    private final GetQuestionMapper getQuestionMapper;
    private final CompanyUtilsService companyUtilsService;

    public String createQuestion(QuestionRequestDto questionRequestDto) {
        User user = commonUtilsService.getCurrentUser();

        Product product = productRepository.findById(questionRequestDto.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));

        Question question = createQuestionMapper.questionMapper(questionRequestDto, product, user);

        questionRepository.save(question);

        return "Question created successfully.";
    }

    public String answerQuestion(Long id, AnswerRequestDto answerRequestDto) {
        Company company = companyUtilsService.getCurrentUserCompany();

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found!"));

        if (!question.getCompany().equals(company)) {
            throw new IllegalArgumentException("You are not authorized to answer this question.");
        }

        Question updatedQuestion = createAnswerMapper.answerMapper(question, answerRequestDto);

        questionRepository.save(updatedQuestion);

        return "Answer created successfully.";
    }

    public List<QuestionResponseDto> listProductQuestions(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));
        List<Question> questions = questionRepository.findByProduct(product);
        return questions.stream()
                .map(getQuestionMapper::questionMapper)
                .toList();
    }

    public List<QuestionResponseDto> listSellerQuestions() {
        Company company = companyUtilsService.getCurrentUserCompany();
        List<Question> questions = questionRepository.findByCompany(company);
        return questions.stream()
                .map(getQuestionMapper::questionMapper)
                .toList();
    }

}

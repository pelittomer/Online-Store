package com.online_store.backend.api.question.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.company.entities.Company;
import com.online_store.backend.api.company.utils.CompanyUtilsService;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.utils.ProductUtilsService;
import com.online_store.backend.api.question.dto.request.AnswerRequestDto;
import com.online_store.backend.api.question.dto.request.QuestionRequestDto;
import com.online_store.backend.api.question.dto.response.QuestionResponseDto;
import com.online_store.backend.api.question.entities.Question;
import com.online_store.backend.api.question.repository.QuestionRepository;
import com.online_store.backend.api.question.utils.QuestionUtilsService;
import com.online_store.backend.api.question.utils.mapper.CreateAnswerMapper;
import com.online_store.backend.api.question.utils.mapper.CreateQuestionMapper;
import com.online_store.backend.api.question.utils.mapper.GetQuestionMapper;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {
    // repositories
    private final QuestionRepository questionRepository;
    // utils
    private final CommonUtilsService commonUtilsService;
    private final ProductUtilsService productUtilsService;
    private final CompanyUtilsService companyUtilsService;
    private final QuestionUtilsService questionUtilsService;
    // mappers
    private final CreateQuestionMapper createQuestionMapper;
    private final CreateAnswerMapper createAnswerMapper;
    private final GetQuestionMapper getQuestionMapper;

    public String createQuestion(QuestionRequestDto dto) {
        User user = commonUtilsService.getCurrentUser();
        log.info("Attempting to create question for product ID: {} by user: {}", dto.getProduct(), user.getEmail());

        Product product = productUtilsService.findProductById(dto.getProduct());

        Question question = createQuestionMapper.questionMapper(dto, product, user);
        questionRepository.save(question);

        log.info("Question created successfully with ID: {} for product ID: {}", question.getId(), product.getId());
        return "Question created successfully.";
    }

    public String answerQuestion(Long id, AnswerRequestDto answerRequestDto) {
        Company company = companyUtilsService.getCurrentUserCompany();
        log.info("Attempting to answer question ID: {} by company: {}", id, company.getName());

        Question question = questionUtilsService.findQuestionById(id);

        if (question.getAnswer() != null) {
            log.warn("Question ID: {} has already been answered.", id);
            throw new IllegalArgumentException("This question has already been answered.");
        }
        if (!question.getCompany().equals(company)) {
            log.warn("Company {} is not authorized to answer question ID: {}", company.getName(), id);
            throw new IllegalArgumentException("You are not authorized to answer this question.");
        }

        Question updatedQuestion = createAnswerMapper.answerMapper(question, answerRequestDto);
        questionRepository.save(updatedQuestion);

        log.info("Answer created successfully for question ID: {}", id);
        return "Answer created successfully.";
    }

    public List<QuestionResponseDto> listProductQuestions(Long productId) {
        log.info("Listing questions for product with ID: {}", productId);
        Product product = productUtilsService.findProductById(productId);
        List<Question> questions = questionRepository.findByProduct(product);
        return questions.stream()
                .map(getQuestionMapper::questionMapper)
                .toList();
    }

    public List<QuestionResponseDto> listSellerQuestions() {
        Company company = companyUtilsService.getCurrentUserCompany();
        log.info("Listing questions for company: {}", company.getName());

        List<Question> questions = questionRepository.findByCompany(company);
        return questions.stream()
                .map(getQuestionMapper::questionMapper)
                .toList();
    }

}
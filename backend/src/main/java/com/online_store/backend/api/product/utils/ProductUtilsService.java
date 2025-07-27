package com.online_store.backend.api.product.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductUtilsService {
        //repositories
        private final ProductRepository productRepository;

        public Map<String, List<MultipartFile>> processDynamicFiles(MultipartHttpServletRequest request) {
                Map<String, List<MultipartFile>> dynamicFiles = new HashMap<>();
                Iterator<String> fileNames = request.getFileNames();

                while (fileNames.hasNext()) {
                        String fileName = fileNames.next();
                        List<MultipartFile> filesForThisKey = request.getFiles(fileName);

                        if (filesForThisKey != null && !filesForThisKey.isEmpty()) {
                                List<MultipartFile> actualFiles = filesForThisKey.stream()
                                                .filter(f -> !f.isEmpty())
                                                .toList();

                                if (!actualFiles.isEmpty()) {
                                        dynamicFiles.put(fileName, actualFiles);
                                }
                        }
                }
                return dynamicFiles;
        }

        public Product findProductById(Long productId) {
                return productRepository.findById(productId)
                                .orElseThrow(() -> {
                                        log.warn("Product with ID {} not found.", productId);
                                        return new EntityNotFoundException("Product not found!");
                                });
        }
}
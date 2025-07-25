package com.online_store.backend.api.product.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductUtilsService {

        public <T> T findEntityById(Function<Long, java.util.Optional<T>> finder, Long id, String entityName) {
                return finder.apply(id)
                                .orElseThrow(() -> new IllegalArgumentException(entityName + " not found!"));
        }

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
}
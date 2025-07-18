package com.online_store.backend.api.favorite.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.favorite.repository.FavoriteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

}

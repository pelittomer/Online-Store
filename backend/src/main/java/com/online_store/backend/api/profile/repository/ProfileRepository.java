package com.online_store.backend.api.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.profile.entities.Profile;
import com.online_store.backend.api.user.entities.User;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUser(User user);
}

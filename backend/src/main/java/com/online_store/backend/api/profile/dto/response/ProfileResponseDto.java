package com.online_store.backend.api.profile.dto.response;

import java.time.LocalDate;

import com.online_store.backend.api.profile.entities.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthOfDate;
    private Gender gender;
    private Long avatar;
}

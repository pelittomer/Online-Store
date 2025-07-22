package com.online_store.backend.api.profile.dto.request;

import java.time.LocalDate;

import com.online_store.backend.api.profile.entities.Gender;

import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDto {
    private String firstName;
    private String lastName;
    private Gender gender;

    @Past(message = "Birth of date must be in the past")
    private LocalDate birthOfDate;
}

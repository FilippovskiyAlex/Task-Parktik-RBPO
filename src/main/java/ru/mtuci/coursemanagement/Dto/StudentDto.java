package ru.mtuci.coursemanagement.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentDto {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    private Long userId;
}

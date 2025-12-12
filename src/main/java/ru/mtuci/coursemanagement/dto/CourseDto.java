package ru.mtuci.coursemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseDto {
    @NotBlank
    private String title;

    @Size(max = 2000)
    private String description;

    private Long teacherId;
}

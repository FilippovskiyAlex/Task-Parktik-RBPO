package ru.mtuci.coursemanagement.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.coursemanagement.dto.StudentDto;
import ru.mtuci.coursemanagement.model.Student;
import ru.mtuci.coursemanagement.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository repo;

    public List<StudentDto> findAllDto() {
        List<StudentDto> dtoList = new ArrayList<>();
        List<Student> eList = repo.findAll();
        for (Student e : eList){
            StudentDto dto = new StudentDto();
            dto.setEmail(e.getEmail());
            dto.setName(e.getName());
            dto.setUserId(e.getUserId());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public void save(StudentDto dto) {
        Student entity = new Student();
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());
        entity.setUserId(dto.getUserId());
        repo.save(entity);
    }

    public Student getStudent(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}

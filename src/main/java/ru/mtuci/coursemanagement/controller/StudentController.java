package ru.mtuci.coursemanagement.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.mtuci.coursemanagement.dto.StudentDto;
import ru.mtuci.coursemanagement.model.Student;
import ru.mtuci.coursemanagement.service.StudentService;

import java.util.List;

@Controller
// @CrossOrigin(origins = "*") -- Настройка уже есть в WebConfig
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @GetMapping("/students")
    public String studentsPage(Model model) {
        model.addAttribute("students", service.findAllDto());
        model.addAttribute("student", new StudentDto());
        return "students";
    }

    @PostMapping("/students")
    public String createStudent(@ModelAttribute @Valid StudentDto st) {
        service.save(st);
        return "redirect:/students";
    }

    @GetMapping("/api/students")
    @ResponseBody
    public List<StudentDto> all() {
        return service.findAllDto();
    }

    @GetMapping("/api/students/{id}")
    @ResponseBody
    public ResponseEntity<StudentDto> one(@PathVariable Long id) {
        Student e = service.getStudent(id);
        if (e == null) {
            return ResponseEntity.notFound().build();
        }
        StudentDto dto = new StudentDto();
        dto.setEmail(e.getEmail());
        dto.setName(e.getName());
        dto.setUserId(e.getUserId());
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/api/students/{id}")
    @ResponseBody
    public ResponseEntity<StudentDto> update(@PathVariable Long id, @RequestBody @Valid @NonNull StudentDto payload) {
        Student e = service.getStudent(id);
        if (e == null) {
            return ResponseEntity.notFound().build();
        }
        if (payload.getEmail() == null || payload.getName() == null || payload.getUserId() == null) {
            return ResponseEntity.notFound().build();
        }
        e.setEmail(payload.getEmail());
        e.setName(payload.getName());
        e.setUserId(payload.getUserId());
        return ResponseEntity.ok().body(payload);
    }

    @DeleteMapping("/api/students/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

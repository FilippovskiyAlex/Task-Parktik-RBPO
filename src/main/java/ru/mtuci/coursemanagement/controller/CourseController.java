package ru.mtuci.coursemanagement.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.mtuci.coursemanagement.dto.CourseDto;
import ru.mtuci.coursemanagement.model.Course;
import ru.mtuci.coursemanagement.service.CourseService;

import java.net.InetAddress;
import java.net.URI;
import java.util.List;


@Slf4j
@Controller
// @CrossOrigin(origins = "*") -- Настройка уже есть в WebConfig
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;

    @GetMapping("/courses")
    public String coursesPage(Model model) {
        model.addAttribute("courses", service.findAllDto());
        model.addAttribute("course", new CourseDto());
        return "courses";
    }

    @PostMapping("/courses")
    public String createCourse(@ModelAttribute @Valid CourseDto c) {
        service.save(c);
        return "redirect:/courses";
    }

    @GetMapping("/api/courses")
    @ResponseBody
    public List<CourseDto> all() {
        return service.findAllDto();
    }

    @GetMapping("/api/courses/{id}")
    @ResponseBody
    public ResponseEntity<CourseDto> one(@PathVariable Long id) {
        Course e = service.getCourse(id);
        if (e == null) {
            return ResponseEntity.notFound().build();
        }
        CourseDto dto = new CourseDto();
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setTeacherId(e.getTeacherId());
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/api/courses/{id}")
    @ResponseBody
    public ResponseEntity<CourseDto> update(@PathVariable Long id, @RequestBody @Valid @NonNull CourseDto payload) {
        Course e = service.getCourse(id);
        if (e == null) {
            return ResponseEntity.notFound().build();
        }
        if (payload.getTitle() == null || payload.getDescription() == null || payload.getTeacherId() == null) {
            return ResponseEntity.notFound().build();
        }
        e.setTitle(payload.getTitle());
        e.setDescription(payload.getDescription());
        e.setTeacherId(payload.getTeacherId());
        return ResponseEntity.ok().body(payload);
    }

    @GetMapping("/api/courses/search")
    @ResponseBody
    public List<Course> search(@RequestParam String title) {
        return service.searchByTitle(title);
    }

    @GetMapping("/api/courses/import")
    @ResponseBody
    public String importFromUrl(@RequestParam String url) {
        // Проверка URI
        try {
            URI uri = new URI(url);
            if (!"https".equals(uri.getScheme())) {
                return "NOT";
            }
            String host = uri.getHost();
            InetAddress inetAddress = InetAddress.getByName(host);
            // Внутренние адреса
            boolean isNotLocal = !inetAddress.isSiteLocalAddress() &&
                    !inetAddress.isLoopbackAddress() &&
                    !inetAddress.isAnyLocalAddress();
            if (!isNotLocal){
                return "NOT";
            }
        } catch (Exception e) {
            return "NOT";
        }
        // Настройка Http соединений
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        RestTemplate rt = new RestTemplate(requestFactory);
        String json = rt.getForObject(url, String.class, 100000); // Размер, который импортируются
        log.info("Импортированы данные курсов");                             // Не указываем данные
        return "OK";
    }
}

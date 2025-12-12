package ru.mtuci.coursemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.mtuci.coursemanagement.dto.CourseDto;
import ru.mtuci.coursemanagement.model.Course;
import ru.mtuci.coursemanagement.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository repo;
    private final JdbcTemplate jdbc;

    public List<CourseDto> findAllDto() {
        List<CourseDto> dtoList = new ArrayList<>();
        List<Course> eList = repo.findAll();
        for (Course e : eList){
            CourseDto dto = new CourseDto();
            dto.setTitle(e.getTitle());
            dto.setDescription(e.getDescription());
            dto.setTeacherId(e.getTeacherId());
            dtoList.add(dto);
            }
        return dtoList;
    }

    public void save(CourseDto dto) {
        Course entity = new Course();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setTeacherId(dto.getTeacherId());
        repo.save(entity);
    }

    public Course getCourse(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<Course> searchByTitle(String title) {
        String sql = "SELECT id, title, description, teacher_id FROM courses WHERE title = ?";
        RowMapper<Course> rm = (rs, i) -> new Course(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getLong("teacher_id")
        );
        return jdbc.query(sql, rm, title);
    }


}

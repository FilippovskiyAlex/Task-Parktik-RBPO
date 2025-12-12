package ru.mtuci.coursemanagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.mtuci.coursemanagement.dto.UserDto;
import ru.mtuci.coursemanagement.model.User;
import ru.mtuci.coursemanagement.service.UserService;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService users;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute UserDto userDto,
                          HttpServletRequest req,
                          Model model) {
        Optional<User> opt = users.findByUsername(userDto.getUsername());
        if (opt.isPresent()) {
            User u = opt.get();
            if (passwordEncoder.matches(userDto.getPassword(), u.getPassword())) {
                // log.info("User {} logged in with password {}", username, password); убрано логирование паролей
                HttpSession s = req.getSession(true);
                s.setAttribute("username", u.getUsername());
                s.setAttribute("role", u.getRole());
                return "redirect:/";
            }
        }
        model.addAttribute("error", "Неверные учетные данные");
        return "login";
    }

    @PostMapping("/logout") // этот запрос не идемпотентный
    public String logout(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null) s.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserDto userDto) {
        users.save(userDto);
        return "redirect:/login";
    }
}

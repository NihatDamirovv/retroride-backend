package az.myapp.retroride.service;

import az.myapp.retroride.dao.repository.UserRepository;
import az.myapp.retroride.dao.utility.User;
import az.myapp.retroride.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Hazırda daxil olmuş istifadəçinin profili
    public UserResponseDto getMyProfile() {
        User user = getCurrentUser();
        return toResponse(user);
    }

    // Profili yenilə (ad, telefon)
    public UserResponseDto updateMyProfile(String name, String phone) {
        User user = getCurrentUser();
        user.setName(name);
        user.setPhone(phone);
        userRepository.save(user);
        return toResponse(user);
    }

    // -----------------------------------------------
    // Köməkçi metodlar
    // -----------------------------------------------

    // SecurityContext-dən hazırki istifadəçini al
    // JwtAuthFilter bu məlumatı hər sorğuda oraya yazır
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    // User entity-ni UserResponse DTO-ya çevir
    // Niyə? — Şifrəni frontend-ə göndərməmək üçün
    private UserResponseDto toResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getCreatedAt()
        );
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
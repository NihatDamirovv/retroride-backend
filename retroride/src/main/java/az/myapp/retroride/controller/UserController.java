package az.myapp.retroride.controller;

import az.myapp.retroride.dao.utility.User;
import az.myapp.retroride.dto.response.UserResponseDto;
import az.myapp.retroride.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/users/me — öz profilim
    // Header: Authorization: Bearer TOKEN
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // PUT /api/users/me — profili yenilə
    // Body: { "name": "Yeni Ad", "phone": "0501234567" }
    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateMyProfile(@RequestBody Map<String, String> body) {
        // Map<String, String> — sadə JSON body oxumaq üçün
        String name = body.get("name");
        String phone = body.get("phone");
        return ResponseEntity.ok(userService.updateMyProfile(name, phone));
    }
}
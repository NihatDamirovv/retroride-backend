package az.myapp.retroride.service;

import az.myapp.retroride.config.JwtUtil;
import az.myapp.retroride.dao.repository.UserRepository;
import az.myapp.retroride.dao.utility.User;
import az.myapp.retroride.dto.request.LoginRequestDto;
import az.myapp.retroride.dto.request.LoginRequestDto;
import az.myapp.retroride.dto.request.RegisterRequestDto;
import az.myapp.retroride.dto.request.RegisterRequestDto;
import az.myapp.retroride.dto.response.AuthResponseDto;
import az.myapp.retroride.dto.response.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterRequestDto request) {
        // Email mövcuddurmu?
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email artıq qeydiyyatdan keçib");
        }

        // İstifadəçi yarat — şifrəni mütləq hash-lə!
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return new AuthResponseDto(token, user.getName(), user.getEmail());
    }

    public AuthResponseDto login(LoginRequestDto request) {
        // Bu metod email + şifrəni yoxlayır
        // Yanlışdırsa avtomatik exception atır
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        String token = jwtUtil.generateToken(user);
        return new AuthResponseDto(token, user.getName(), user.getEmail());
    }
}
package az.myapp.retroride.dto.request;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String name;
    private String email;
    private String password;
    private String phone;
}

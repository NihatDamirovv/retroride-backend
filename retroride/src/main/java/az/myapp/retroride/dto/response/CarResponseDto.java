package az.myapp.retroride.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

    //masin melumatlari front end e qayidir
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarResponseDto {

    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private BigDecimal price;
    private Integer mileage;
    private String color;
    private String description;
    private String imageUrl;
    private String status;           //enum
    private LocalDateTime createdAt;

    // owner infos (User entity-sinden)
    private String ownerName;
    private String ownerPhone;
}

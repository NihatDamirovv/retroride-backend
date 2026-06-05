package az.myapp.retroride.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
//yeni elan yaratmaq ucun gelen melumatlar
public class CarRequestDto {
    private String brand;
    private String model;
    private Integer year;
    private BigDecimal price;
    private Integer mileage;
    private String color;
    private String description;
    private String imageUrl;
}

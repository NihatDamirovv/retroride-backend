package az.myapp.retroride.service;

import az.myapp.retroride.dao.repository.CarRepository;
import az.myapp.retroride.dao.utility.Car;
import az.myapp.retroride.dao.utility.User;
import az.myapp.retroride.dto.request.CarRequestDto;
import az.myapp.retroride.dto.response.CarResponseDto;
import az.myapp.retroride.dto.response.CarResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    // Bütün aktiv elanlar — ana səhifə
    public List<CarResponseDto> getAllActiveCars() {
        return carRepository.findByStatus(Car.CarStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Bir elanın detalları
    public CarResponseDto getCarById(Long id) {
        Car car = findCarOrThrow(id);
        return toResponse(car);
    }

    // Mənim elanlarım
    public List<CarResponseDto> getMyCars() {
        User user = getCurrentUser();
        return carRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Yeni elan yarat
    public CarResponseDto createCar(CarRequestDto request) {
        User user = getCurrentUser();

        Car car = Car.builder()
                .user(user)
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .price(request.getPrice())
                .mileage(request.getMileage())
                .color(request.getColor())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .status(Car.CarStatus.ACTIVE)
                .build();

        return toResponse(carRepository.save(car));
    }

    // Elanı yenilə — yalnız öz elanın
    public CarResponseDto updateCar(Long id, CarRequestDto request) {
        Car car = findCarOrThrow(id);
        checkOwnership(car);

        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setPrice(request.getPrice());
        car.setMileage(request.getMileage());
        car.setColor(request.getColor());
        car.setDescription(request.getDescription());
        car.setImageUrl(request.getImageUrl());

        return toResponse(carRepository.save(car));
    }

    // Elanı sil — yalnız öz elanın
    public void deleteCar(Long id) {
        Car car = findCarOrThrow(id);
        checkOwnership(car);
        carRepository.delete(car);
    }

    private Car findCarOrThrow(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Elan tapılmadı: " + id));
    }

    private void checkOwnership(Car car) {
        User user = getCurrentUser();
        if (!car.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu elan sizin deyil");
        }
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    // Car entity → CarResponse DTO
    private CarResponseDto toResponse(Car car) {
        return new CarResponseDto(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getPrice(),
                car.getMileage(),
                car.getColor(),
                car.getDescription(),
                car.getImageUrl(),
                car.getStatus().name(),
                car.getCreatedAt(),
                car.getUser().getName(),
                car.getUser().getPhone()
        );
    }
}
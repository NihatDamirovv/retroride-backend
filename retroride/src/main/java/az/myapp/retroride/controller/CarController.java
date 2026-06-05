package az.myapp.retroride.controller;
import az.myapp.retroride.dto.request.CarRequestDto;
import az.myapp.retroride.dto.response.CarResponseDto;
import az.myapp.retroride.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;


    @GetMapping
    public ResponseEntity<List<CarResponseDto>> getAllCars() {
        return ResponseEntity.ok(carService.getAllActiveCars());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    // GET /api/cars/my — mənim elanlarım (token lazımdır)
    @GetMapping("/my")
    public ResponseEntity<List<CarResponseDto>> getMyCars() {
        return ResponseEntity.ok(carService.getMyCars());
    }

    // POST /api/cars — yeni elan yarat (token lazımdır)
    @PostMapping
    public ResponseEntity<CarResponseDto> createCar(@RequestBody CarRequestDto request) {
        return ResponseEntity.ok(carService.createCar(request));
    }

    // PUT /api/cars/5 — elanı yenilə (token lazımdır, yalnız öz elanın)public
    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDto> updateCar(@PathVariable Long id,
                                                 @RequestBody CarRequestDto request) {
        return ResponseEntity.ok(carService.updateCar(id, request));
    }

    // DELETE /api/cars/5 — elanı sil (token lazımdır, yalnız öz elanın)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build(); // 204
    }
}